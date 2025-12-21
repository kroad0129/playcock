package com.playcock.repository;

import com.playcock.domain.session.WaitingTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingTeamRepository extends JpaRepository<WaitingTeam, Long> {
    List<WaitingTeam> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
