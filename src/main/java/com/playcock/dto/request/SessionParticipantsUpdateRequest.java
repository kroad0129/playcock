package com.playcock.dto.request;

import java.util.List;

public class SessionParticipantsUpdateRequest {

    private List<Long> addIds;
    private List<Long> removeIds;

    public SessionParticipantsUpdateRequest() {}

    public List<Long> getAddIds() { return addIds; }
    public void setAddIds(List<Long> addIds) { this.addIds = addIds; }

    public List<Long> getRemoveIds() { return removeIds; }
    public void setRemoveIds(List<Long> removeIds) { this.removeIds = removeIds; }
}
