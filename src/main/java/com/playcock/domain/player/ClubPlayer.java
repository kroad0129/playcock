package com.playcock.domain.player;

import jakarta.persistence.*;

@Entity
@Table(name = "club_player")
public class ClubPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PlayerType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Gender gender;

    protected ClubPlayer() {
        // JPA가 DB에서 조회할 때 객체 생성에 필요
    }

    public ClubPlayer(String name, PlayerType type, Gender gender) {
        this.name = name;
        this.type = type;
        this.gender = gender;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public PlayerType getType() { return type; }
    public Gender getGender() { return gender; }
}
