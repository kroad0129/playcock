package com.playcock.service;

import com.playcock.domain.player.ClubPlayer;
import com.playcock.domain.session.Session;
import com.playcock.domain.session.SessionParticipant;
import com.playcock.domain.session.SessionStatus;
import com.playcock.dto.request.SessionStartRequest;
import com.playcock.dto.response.SessionResponse;
import com.playcock.repository.ClubPlayerRepository;
import com.playcock.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ClubPlayerRepository clubPlayerRepository;

    public SessionService(SessionRepository sessionRepository, ClubPlayerRepository clubPlayerRepository) {
        this.sessionRepository = sessionRepository;
        this.clubPlayerRepository = clubPlayerRepository;
    }

    public SessionResponse start(SessionStartRequest req) {
        // 1) 이미 ACTIVE 세션이 있으면 시작 못 하게 막기
        sessionRepository.findFirstByStatusOrderByStartedAtDesc(SessionStatus.ACTIVE)
                .ifPresent(s -> { throw new IllegalStateException("이미 진행 중인 세션이 있습니다."); });

        // 2) 참가자 id로 ClubPlayer 조회
        List<Long> ids = req.getParticipantClubPlayerIds();
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("participantClubPlayerIds는 비어 있을 수 없습니다.");
        }

        List<ClubPlayer> players = clubPlayerRepository.findAllById(ids);
        if (players.size() != ids.size()) {
            throw new IllegalArgumentException("존재하지 않는 clubPlayerId가 포함되어 있습니다.");
        }

        // 3) Session 생성 + 참가자 연결
        Session session = new Session(LocalDateTime.now());
        for (ClubPlayer p : players) {
            session.addParticipant(new SessionParticipant(p));
        }

        Session saved = sessionRepository.save(session);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SessionResponse getCurrent() {
        Session session = sessionRepository.findFirstByStatusOrderByStartedAtDesc(SessionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("진행 중인 세션이 없습니다."));
        return toResponse(session);
    }

    private SessionResponse toResponse(Session session) {
        List<SessionResponse.Participant> participants = session.getParticipants().stream()
                .map(sp -> {
                    ClubPlayer p = sp.getClubPlayer();
                    return new SessionResponse.Participant(p.getId(), p.getName(), p.getType(), p.getGender());
                })
                .toList();

        return new SessionResponse(session.getId(), session.getStartedAt(), participants);
    }
}
