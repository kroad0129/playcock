package com.playcock.repository;

import com.playcock.domain.session.ParticipantLocation;
import com.playcock.domain.session.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    List<SessionParticipant> findBySessionId(Long sessionId);

    List<SessionParticipant> findBySessionIdAndLocation(Long sessionId, ParticipantLocation location);

    List<SessionParticipant> findBySessionIdAndWaitingTeamId(Long sessionId, Long waitingTeamId);

    List<SessionParticipant> findBySessionIdAndMatchId(Long sessionId, Long matchId);

    Optional<SessionParticipant> findBySessionIdAndClubPlayerId(Long sessionId, Long clubPlayerId);
}
