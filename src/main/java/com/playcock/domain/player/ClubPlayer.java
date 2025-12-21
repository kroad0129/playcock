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

    // ✅ 소프트 삭제 플래그
    @Column(nullable = false)
    private boolean active = true;

    protected ClubPlayer() {
        // JPA 기본 생성자
    }

    public ClubPlayer(String name, PlayerType type, Gender gender) {
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.active = true;
    }

    // ✅ 소프트 삭제
    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() { return active; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public PlayerType getType() { return type; }
    public Gender getGender() { return gender; }
}
