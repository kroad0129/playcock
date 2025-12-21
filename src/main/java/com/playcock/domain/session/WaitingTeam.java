package com.playcock.domain.session;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_team")
public class WaitingTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected WaitingTeam() {}

    public WaitingTeam(Session session, LocalDateTime createdAt) {
        this.session = session;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Session getSession() { return session; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
