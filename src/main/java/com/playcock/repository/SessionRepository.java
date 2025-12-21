package com.playcock.repository;

import com.playcock.domain.session.Session;
import com.playcock.domain.session.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findFirstByStatusOrderByStartedAtDesc(SessionStatus status);
}
