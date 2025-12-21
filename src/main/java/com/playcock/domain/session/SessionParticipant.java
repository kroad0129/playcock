package com.playcock.domain.session;

import com.playcock.domain.player.ClubPlayer;
import jakarta.persistence.*;

@Entity
@Table(name = "session_participant")
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_player_id", nullable = false)
    private ClubPlayer clubPlayer;

    protected SessionParticipant() {}

    public SessionParticipant(ClubPlayer clubPlayer) {
        this.clubPlayer = clubPlayer;
    }

    void setSession(Session session) {
        this.session = session;
    }

    public Long getId() { return id; }
    public Session getSession() { return session; }
    public ClubPlayer getClubPlayer() { return clubPlayer; }
}
