package com.playcock.dto.response;

import com.playcock.domain.player.Gender;
import com.playcock.domain.player.PlayerType;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardResponse {

    private final SessionInfo session;
    private final List<PlayerCard> list;
    private final List<WaitingTeamCard> waitingTeams;
    private final List<MatchCard> playingMatches;

    public DashboardResponse(SessionInfo session, List<PlayerCard> list,
                             List<WaitingTeamCard> waitingTeams, List<MatchCard> playingMatches) {
        this.session = session;
        this.list = list;
        this.waitingTeams = waitingTeams;
        this.playingMatches = playingMatches;
    }

    public SessionInfo getSession() { return session; }
    public List<PlayerCard> getList() { return list; }
    public List<WaitingTeamCard> getWaitingTeams() { return waitingTeams; }
    public List<MatchCard> getPlayingMatches() { return playingMatches; }

    public static class SessionInfo {
        private final Long sessionId;
        private final LocalDateTime startedAt;
        private final Integer totalMatches;

        public SessionInfo(Long sessionId, LocalDateTime startedAt, Integer totalMatches) {
            this.sessionId = sessionId;
            this.startedAt = startedAt;
            this.totalMatches = totalMatches;
        }

        public Long getSessionId() { return sessionId; }
        public LocalDateTime getStartedAt() { return startedAt; }
        public Integer getTotalMatches() { return totalMatches; }
    }

    public static class PlayerCard {
        private final Long clubPlayerId;
        private final String name;
        private final PlayerType type;
        private final Gender gender;
        private final LocalDateTime lastPlayedAt;
        private final long restSeconds;

        private final int maleDoubleCount;
        private final int femaleDoubleCount;
        private final int mixedDoubleCount;
        private final int totalMatchCount;

        public PlayerCard(Long clubPlayerId, String name, PlayerType type, Gender gender,
                          LocalDateTime lastPlayedAt, long restSeconds,
                          int maleDoubleCount, int femaleDoubleCount, int mixedDoubleCount, int totalMatchCount) {
            this.clubPlayerId = clubPlayerId;
            this.name = name;
            this.type = type;
            this.gender = gender;
            this.lastPlayedAt = lastPlayedAt;
            this.restSeconds = restSeconds;
            this.maleDoubleCount = maleDoubleCount;
            this.femaleDoubleCount = femaleDoubleCount;
            this.mixedDoubleCount = mixedDoubleCount;
            this.totalMatchCount = totalMatchCount;
        }

        public Long getClubPlayerId() { return clubPlayerId; }
        public String getName() { return name; }
        public PlayerType getType() { return type; }
        public Gender getGender() { return gender; }
        public LocalDateTime getLastPlayedAt() { return lastPlayedAt; }
        public long getRestSeconds() { return restSeconds; }

        public int getMaleDoubleCount() { return maleDoubleCount; }
        public int getFemaleDoubleCount() { return femaleDoubleCount; }
        public int getMixedDoubleCount() { return mixedDoubleCount; }
        public int getTotalMatchCount() { return totalMatchCount; }
    }

    public static class WaitingTeamCard {
        private final Long waitingTeamId;
        private final LocalDateTime createdAt;
        private final List<PlayerCard> members;

        public WaitingTeamCard(Long waitingTeamId, LocalDateTime createdAt, List<PlayerCard> members) {
            this.waitingTeamId = waitingTeamId;
            this.createdAt = createdAt;
            this.members = members;
        }

        public Long getWaitingTeamId() { return waitingTeamId; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public List<PlayerCard> getMembers() { return members; }
    }

    public static class MatchCard {
        private final Long matchId;
        private final Integer matchNo;
        private final LocalDateTime startedAt;
        private final List<PlayerCard> members;

        public MatchCard(Long matchId, Integer matchNo, LocalDateTime startedAt, List<PlayerCard> members) {
            this.matchId = matchId;
            this.matchNo = matchNo;
            this.startedAt = startedAt;
            this.members = members;
        }

        public Long getMatchId() { return matchId; }
        public Integer getMatchNo() { return matchNo; }
        public LocalDateTime getStartedAt() { return startedAt; }
        public List<PlayerCard> getMembers() { return members; }

    }
}
