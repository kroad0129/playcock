package com.playcock.repository;

import com.playcock.domain.player.ClubPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubPlayerRepository extends JpaRepository<ClubPlayer, Long> {

    List<ClubPlayer> findByActiveTrueOrderByIdAsc();
}
