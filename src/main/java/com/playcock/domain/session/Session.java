package com.playcock.domain.session;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SessionStatus status;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    // ✅ "시작된 경기 수" 누적 카운트
    @Column(nullable = false)
    private Integer totalMatches;

    protected Session() {}

    public Session(LocalDateTime startedAt) {
        this.status = SessionStatus.ACTIVE;
        this.startedAt = startedAt;
        this.totalMatches = 0;
    }

    public void end(LocalDateTime endedAt) {
        this.status = SessionStatus.ENDED;
        this.endedAt = endedAt;
    }

    public int nextMatchNoAndIncrease() {
        this.totalMatches = this.totalMatches + 1;
        return this.totalMatches;
    }

    public Long getId() { return id; }
    public SessionStatus getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
    public Integer getTotalMatches() { return totalMatches; }
}
