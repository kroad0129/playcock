package com.playcock.repository;

import com.playcock.domain.session.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {
    List<SessionParticipant> findBySessionId(Long sessionId);
    void deleteBySessionIdAndClubPlayerIdIn(Long sessionId, List<Long> clubPlayerIds);
}
