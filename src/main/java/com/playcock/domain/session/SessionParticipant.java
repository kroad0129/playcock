package com.playcock.domain.session;

import com.playcock.domain.player.ClubPlayer;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_participant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_session_clubplayer", columnNames = {"session_id", "club_player_id"})
        })
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 세션 참가자인가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // 어떤 플레이어인가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_player_id", nullable = false)
    private ClubPlayer clubPlayer;

    // LIST / WAITING / PLAYING
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ParticipantLocation location;

    // 휴식시간 기준(세션 시작 시각으로 초기화, 경기 종료 시에만 갱신)
    @Column(nullable = false)
    private LocalDateTime lastPlayedAt;

    // WAITING 소속 팀(없으면 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_team_id")
    private WaitingTeam waitingTeam;

    // PLAYING 소속 경기(없으면 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(nullable = false)
    private Integer maleDoubleCount;

    @Column(nullable = false)
    private Integer femaleDoubleCount;

    @Column(nullable = false)
    private Integer mixedDoubleCount;

    protected SessionParticipant() {}

    public SessionParticipant(Session session, ClubPlayer clubPlayer, LocalDateTime sessionStartedAt) {
        this.session = session;
        this.clubPlayer = clubPlayer;
        this.location = ParticipantLocation.LIST;
        this.lastPlayedAt = sessionStartedAt;

        this.maleDoubleCount = 0;
        this.femaleDoubleCount = 0;
        this.mixedDoubleCount = 0;
    }

    public void moveToList() {
        this.location = ParticipantLocation.LIST;
        this.waitingTeam = null;
        this.match = null;
    }

    public void moveToWaiting(WaitingTeam team) {
        this.location = ParticipantLocation.WAITING;
        this.waitingTeam = team;
        this.match = null;
    }

    public void moveToPlaying(Match match) {
        this.location = ParticipantLocation.PLAYING;
        this.match = match;
        this.waitingTeam = null;
    }

    public void updateLastPlayedAt(LocalDateTime endedAt) {
        this.lastPlayedAt = endedAt;
    }

    public void increaseByMatchType(MatchType type) {
        if (type == MatchType.MALE_DOUBLE) {
            this.maleDoubleCount++;
        } else if (type == MatchType.FEMALE_DOUBLE) {
            this.femaleDoubleCount++;
        } else {
            this.mixedDoubleCount++;
        }
    }

    public int getMaleDoubleCount() {
        return maleDoubleCount;
    }

    public int getFemaleDoubleCount() {
        return femaleDoubleCount;
    }

    public int getMixedDoubleCount() {
        return mixedDoubleCount;
    }

    public int getTotalMatchCount() {
        return maleDoubleCount + femaleDoubleCount + mixedDoubleCount;
    }

    public Long getId() { return id; }
    public Session getSession() { return session; }
    public ClubPlayer getClubPlayer() { return clubPlayer; }
    public ParticipantLocation getLocation() { return location; }
    public LocalDateTime getLastPlayedAt() { return lastPlayedAt; }
    public WaitingTeam getWaitingTeam() { return waitingTeam; }
    public Match getMatch() { return match; }
}
