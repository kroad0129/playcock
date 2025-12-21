package com.playcock.dto.response;

import com.playcock.domain.player.Gender;
import com.playcock.domain.player.PlayerType;

public class ClubPlayerResponse {

    private final Long id;
    private final String name;
    private final PlayerType type;
    private final Gender gender;

    public ClubPlayerResponse(Long id, String name, PlayerType type, Gender gender) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.gender = gender;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public PlayerType getType() { return type; }
    public Gender getGender() { return gender; }
}
