package com.playcock.service;

import com.playcock.domain.player.ClubPlayer;
import com.playcock.domain.player.Gender;
import com.playcock.domain.session.*;
import com.playcock.dto.request.MatchStartRequest;
import com.playcock.dto.request.SessionStartRequest;
import com.playcock.dto.request.WaitingTeamCreateRequest;
import com.playcock.dto.response.DashboardResponse;
import com.playcock.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionFlowService {

    private final SessionRepository sessionRepository;
    private final ClubPlayerRepository clubPlayerRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final WaitingTeamRepository waitingTeamRepository;
    private final MatchRepository matchRepository;

    public SessionFlowService(SessionRepository sessionRepository,
                              ClubPlayerRepository clubPlayerRepository,
                              SessionParticipantRepository sessionParticipantRepository,
                              WaitingTeamRepository waitingTeamRepository,
                              MatchRepository matchRepository) {
        this.sessionRepository = sessionRepository;
        this.clubPlayerRepository = clubPlayerRepository;
        this.sessionParticipantRepository = sessionParticipantRepository;
        this.waitingTeamRepository = waitingTeamRepository;
        this.matchRepository = matchRepository;
    }

    // 1) 세션 시작
    public DashboardResponse startSession(SessionStartRequest req) {
        sessionRepository.findFirstByStatusOrderByStartedAtDesc(SessionStatus.ACTIVE)
                .ifPresent(s -> { throw new IllegalStateException("이미 진행 중인 세션이 있습니다."); });

        List<Long> ids = req.getParticipantClubPlayerIds();
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("participantClubPlayerIds는 비어 있을 수 없습니다.");
        }

        List<ClubPlayer> players = clubPlayerRepository.findAllById(ids);
        if (players.size() != ids.size()) {
            throw new IllegalArgumentException("존재하지 않는 clubPlayerId가 포함되어 있습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        Session session = sessionRepository.save(new Session(now));

        for (ClubPlayer p : players) {
            sessionParticipantRepository.save(new SessionParticipant(session, p, session.getStartedAt()));
        }

        return dashboard();
    }

    // 2) 대시보드(현재 세션 전체 상태)
    @Transactional(readOnly = true)
    public DashboardResponse dashboard() {
        Session session = getActiveSessionOrThrow();

        LocalDateTime now = LocalDateTime.now();

        List<SessionParticipant> all = sessionParticipantRepository.findBySessionId(session.getId());

        // LIST
        List<DashboardResponse.PlayerCard> list = all.stream()
                .filter(sp -> sp.getLocation() == ParticipantLocation.LIST)
                .map(sp -> toPlayerCard(sp, now))
                .toList();

        // WAITING 팀 큐
        List<WaitingTeam> teams = waitingTeamRepository.findBySessionIdOrderByCreatedAtAsc(session.getId());
        Map<Long, List<SessionParticipant>> teamMembersMap = all.stream()
                .filter(sp -> sp.getWaitingTeam() != null)
                .collect(Collectors.groupingBy(sp -> sp.getWaitingTeam().getId()));

        List<DashboardResponse.WaitingTeamCard> waitingCards = teams.stream()
                .map(team -> {
                    List<DashboardResponse.PlayerCard> members = teamMembersMap.getOrDefault(team.getId(), List.of())
                            .stream().map(sp -> toPlayerCard(sp, now)).toList();
                    return new DashboardResponse.WaitingTeamCard(team.getId(), team.getCreatedAt(), members);
                })
                .toList();

        // PLAYING 경기
        List<Match> matches = matchRepository.findBySessionIdAndEndedAtIsNull(session.getId());
        Map<Long, List<SessionParticipant>> matchMembersMap = all.stream()
                .filter(sp -> sp.getMatch() != null)
                .collect(Collectors.groupingBy(sp -> sp.getMatch().getId()));

        List<DashboardResponse.MatchCard> matchCards = matches.stream()
                .sorted(Comparator.comparing(Match::getMatchNo))
                .map(m -> {
                    List<DashboardResponse.PlayerCard> members = matchMembersMap.getOrDefault(m.getId(), List.of())
                            .stream().map(sp -> toPlayerCard(sp, now)).toList();
                    return new DashboardResponse.MatchCard(m.getId(), m.getMatchNo(), m.getStartedAt(), members);
                })
                .toList();

        DashboardResponse.SessionInfo sessionInfo =
                new DashboardResponse.SessionInfo(session.getId(), session.getStartedAt(), session.getTotalMatches());

        return new DashboardResponse(sessionInfo, list, waitingCards, matchCards);
    }

    // 3) WAITING 팀 생성: LIST에서 1~4명 선택 → WAITING 팀 큐에 추가
    public DashboardResponse createWaitingTeam(WaitingTeamCreateRequest req) {
        Session session = getActiveSessionOrThrow();

        List<Long> ids = req.getClubPlayerIds();
        if (ids == null || ids.isEmpty() || ids.size() > 4) {
            throw new IllegalArgumentException("clubPlayerIds는 1~4명이어야 합니다.");
        }

        // 중복 제거 방지
        Set<Long> unique = new HashSet<>(ids);
        if (unique.size() != ids.size()) {
            throw new IllegalArgumentException("중복된 clubPlayerId가 포함되어 있습니다.");
        }

        // 참가자 검증 + 반드시 LIST여야 함
        List<SessionParticipant> participants = ids.stream()
                .map(id -> sessionParticipantRepository.findBySessionIdAndClubPlayerId(session.getId(), id)
                        .orElseThrow(() -> new IllegalArgumentException("세션에 없는 clubPlayerId가 포함되어 있습니다: " + id)))
                .toList();

        for (SessionParticipant sp : participants) {
            if (sp.getLocation() != ParticipantLocation.LIST) {
                throw new IllegalStateException("LIST에 없는 플레이어가 포함되어 있습니다: " + sp.getClubPlayer().getName());
            }
        }

        WaitingTeam team = waitingTeamRepository.save(new WaitingTeam(session, LocalDateTime.now()));
        for (SessionParticipant sp : participants) {
            sp.moveToWaiting(team);
        }

        return dashboard();
    }

    // 4) WAITING 팀 삭제: 팀 해체 → 멤버 LIST 복귀 (lastPlayedAt 변경 금지)
    public DashboardResponse deleteWaitingTeam(Long teamId) {
        Session session = getActiveSessionOrThrow();

        WaitingTeam team = waitingTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("waitingTeamId가 존재하지 않습니다."));
        if (!team.getSession().getId().equals(session.getId())) {
            throw new IllegalArgumentException("현재 세션의 waitingTeam이 아닙니다.");
        }

        List<SessionParticipant> members = sessionParticipantRepository.findBySessionIdAndWaitingTeamId(session.getId(), teamId);
        for (SessionParticipant sp : members) {
            sp.moveToList(); // lastPlayedAt 그대로 유지
        }

        waitingTeamRepository.delete(team);

        return dashboard();
    }

    // 5) 경기 시작: WAITING 팀 → PLAYING + 코트 자동 배정 + Match 생성
    public DashboardResponse startMatch(MatchStartRequest req) {
        Session session = getActiveSessionOrThrow();

        Long teamId = req.getWaitingTeamId();
        if (teamId == null) throw new IllegalArgumentException("waitingTeamId가 필요합니다.");

        WaitingTeam team = waitingTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("waitingTeamId가 존재하지 않습니다."));
        if (!team.getSession().getId().equals(session.getId())) {
            throw new IllegalArgumentException("현재 세션의 waitingTeam이 아닙니다.");
        }

        List<SessionParticipant> members =
                sessionParticipantRepository.findBySessionIdAndWaitingTeamId(session.getId(), teamId);

        if (members.isEmpty()) throw new IllegalStateException("대기팀 멤버가 없습니다.");
        if (members.size() > 4) throw new IllegalStateException("대기팀 멤버는 최대 4명입니다.");

        for (SessionParticipant sp : members) {
            if (sp.getLocation() != ParticipantLocation.WAITING) {
                throw new IllegalStateException("WAITING 상태가 아닌 멤버가 포함되어 있습니다.");
            }
        }

        int matchNo = session.nextMatchNoAndIncrease();

        MatchType matchType = determineMatchType(members);

        Match match = matchRepository.save(new Match(session, matchNo, matchType, LocalDateTime.now()));


        for (SessionParticipant sp : members) {
            sp.moveToPlaying(match);
        }

        waitingTeamRepository.delete(team);

        return dashboard();
    }

    // 6) 경기 종료: PLAYING → LIST 복귀 + lastPlayedAt 갱신(종료 시각)
    public DashboardResponse endMatch(Long matchId) {
        Session session = getActiveSessionOrThrow();

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("matchId가 존재하지 않습니다."));
        if (!match.getSession().getId().equals(session.getId())) {
            throw new IllegalArgumentException("현재 세션의 match가 아닙니다.");
        }
        if (match.getEndedAt() != null) {
            throw new IllegalStateException("이미 종료된 경기입니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        match.end(now);

        List<SessionParticipant> members = sessionParticipantRepository.findBySessionIdAndMatchId(session.getId(), matchId);
        MatchType matchType = match.getMatchType(); // ✅ 이 경기 타입

        for (SessionParticipant sp : members) {
            sp.updateLastPlayedAt(now);        // 휴식시간 기준 갱신(경기 종료)
            sp.increaseByMatchType(matchType); // ✅ 남복/여복/혼복 중 하나 증가
            sp.moveToList();                   // LIST 복귀
        }


        return dashboard();
    }

    // 7) 세션 종료(권장: 진행 중 경기가 없어야 종료 가능)
    public void endSession() {
        Session session = getActiveSessionOrThrow();
        List<Match> playing = matchRepository.findBySessionIdAndEndedAtIsNull(session.getId());
        if (!playing.isEmpty()) {
            throw new IllegalStateException("진행 중 경기가 있어 세션을 종료할 수 없습니다.");
        }
        session.end(LocalDateTime.now());
        sessionRepository.save(session);
    }

    // ===== helper =====

    private Session getActiveSessionOrThrow() {
        return sessionRepository.findFirstByStatusOrderByStartedAtDesc(SessionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("진행 중인 세션이 없습니다."));
    }

    private DashboardResponse.PlayerCard toPlayerCard(SessionParticipant sp, LocalDateTime now) {
        ClubPlayer p = sp.getClubPlayer();
        long restSeconds = Duration.between(sp.getLastPlayedAt(), now).getSeconds();
        if (restSeconds < 0) restSeconds = 0;
        return new DashboardResponse.PlayerCard(
                p.getId(),
                p.getName(),
                p.getType(),
                p.getGender(),
                sp.getLastPlayedAt(),
                restSeconds,
                sp.getMaleDoubleCount(),
                sp.getFemaleDoubleCount(),
                sp.getMixedDoubleCount(),
                sp.getTotalMatchCount()
        );

    }

    private MatchType determineMatchType(List<SessionParticipant> members) {
        int size = members.size();

        long male = members.stream()
                .filter(sp -> sp.getClubPlayer().getGender() == Gender.MALE)
                .count();

        long female = members.stream()
                .filter(sp -> sp.getClubPlayer().getGender() == Gender.FEMALE)
                .count();

        if (size == 4 && female == 4) return MatchType.FEMALE_DOUBLE;
        if (size == 4 && male == 4) return MatchType.MALE_DOUBLE;
        return MatchType.MIXED_DOUBLE;
    }

}
