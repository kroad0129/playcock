package com.playcock.domain.session;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SessionStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionParticipant> participants = new ArrayList<>();

    protected Session() {}

    public Session(LocalDateTime startedAt) {
        this.status = SessionStatus.ACTIVE;
        this.startedAt = startedAt;
    }

    public void end(LocalDateTime endedAt) {
        this.status = SessionStatus.ENDED;
        this.endedAt = endedAt;
    }

    public void addParticipant(SessionParticipant participant) {
        participants.add(participant);
        participant.setSession(this);
    }

    public Long getId() { return id; }
    public SessionStatus getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
    public List<SessionParticipant> getParticipants() { return participants; }
}

