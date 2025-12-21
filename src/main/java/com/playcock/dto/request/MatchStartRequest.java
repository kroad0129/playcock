package com.playcock.dto.request;

public class MatchStartRequest {

    private Long waitingTeamId;

    public MatchStartRequest() {}

    public Long getWaitingTeamId() { return waitingTeamId; }
    public void setWaitingTeamId(Long waitingTeamId) { this.waitingTeamId = waitingTeamId; }
}
