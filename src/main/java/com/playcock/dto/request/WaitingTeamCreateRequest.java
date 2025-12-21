package com.playcock.dto.request;

import java.util.List;

public class WaitingTeamCreateRequest {

    private List<Long> clubPlayerIds;

    public WaitingTeamCreateRequest() {}

    public List<Long> getClubPlayerIds() { return clubPlayerIds; }
    public void setClubPlayerIds(List<Long> clubPlayerIds) { this.clubPlayerIds = clubPlayerIds; }
}
