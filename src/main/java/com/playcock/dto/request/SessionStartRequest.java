package com.playcock.dto.request;

import java.util.List;

public class SessionStartRequest {

    private List<Long> participantClubPlayerIds;

    public SessionStartRequest() {}

    public List<Long> getParticipantClubPlayerIds() { return participantClubPlayerIds; }
    public void setParticipantClubPlayerIds(List<Long> participantClubPlayerIds) {
        this.participantClubPlayerIds = participantClubPlayerIds;
    }
}
