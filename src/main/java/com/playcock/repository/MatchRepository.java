package com.playcock.repository;

import com.playcock.domain.session.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findBySessionIdAndEndedAtIsNull(Long sessionId);
}
