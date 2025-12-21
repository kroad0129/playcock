package com.playcock.domain.session;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_game")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private Integer matchNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchType matchType;


    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    protected Match() {}

    public Match(Session session, Integer matchNo, MatchType matchType, LocalDateTime startedAt) {
        this.session = session;
        this.matchNo = matchNo;
        this.matchType = matchType;
        this.startedAt = startedAt;
    }

    public void end(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Long getId() { return id; }
    public Session getSession() { return session; }
    public Integer getMatchNo() { return matchNo; }
    public MatchType getMatchType() { return matchType; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
}
