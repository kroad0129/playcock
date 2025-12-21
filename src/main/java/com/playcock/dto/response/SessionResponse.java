package com.playcock.dto.response;

import com.playcock.domain.player.Gender;
import com.playcock.domain.player.PlayerType;

import java.time.LocalDateTime;
import java.util.List;

public class SessionResponse {

    private final Long sessionId;
    private final LocalDateTime startedAt;
    private final List<Participant> participants;

    public SessionResponse(Long sessionId, LocalDateTime startedAt, List<Participant> participants) {
        this.sessionId = sessionId;
        this.startedAt = startedAt;
        this.participants = participants;
    }

    public Long getSessionId() { return sessionId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public List<Participant> getParticipants() { return participants; }

    public static class Participant {
        private final Long clubPlayerId;
        private final String name;
        private final PlayerType type;
        private final Gender gender;

        public Participant(Long clubPlayerId, String name, PlayerType type, Gender gender) {
            this.clubPlayerId = clubPlayerId;
            this.name = name;
            this.type = type;
            this.gender = gender;
        }

        public Long getClubPlayerId() { return clubPlayerId; }
        public String getName() { return name; }
        public PlayerType getType() { return type; }
        public Gender getGender() { return gender; }
    }
}
