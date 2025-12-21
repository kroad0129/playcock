package com.playcock.repository;

import com.playcock.domain.player.ClubPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubPlayerRepository extends JpaRepository<ClubPlayer, Long> {
}