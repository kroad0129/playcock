package com.playcock.dto.request;

import com.playcock.domain.player.Gender;
import com.playcock.domain.player.PlayerType;

public class ClubPlayerCreateRequest {

    private String name;
    private PlayerType type;
    private Gender gender;

    public ClubPlayerCreateRequest() {}

    public String getName() { return name; }
    public PlayerType getType() { return type; }
    public Gender getGender() { return gender; }

    public void setName(String name) { this.name = name; }
    public void setType(PlayerType type) { this.type = type; }
    public void setGender(Gender gender) { this.gender = gender; }
}
