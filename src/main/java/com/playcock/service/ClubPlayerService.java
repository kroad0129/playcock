package com.playcock.service;

import com.playcock.domain.player.ClubPlayer;
import com.playcock.dto.request.ClubPlayerCreateRequest;
import com.playcock.dto.response.ClubPlayerResponse;
import com.playcock.repository.ClubPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClubPlayerService {

    private final ClubPlayerRepository clubPlayerRepository;

    public ClubPlayerService(ClubPlayerRepository clubPlayerRepository) {
        this.clubPlayerRepository = clubPlayerRepository;
    }

    public ClubPlayerResponse create(ClubPlayerCreateRequest req) {
        ClubPlayer saved = clubPlayerRepository.save(
                new ClubPlayer(req.getName(), req.getType(), req.getGender())
        );
        return new ClubPlayerResponse(saved.getId(), saved.getName(), saved.getType(), saved.getGender());
    }

    @Transactional(readOnly = true)
    public List<ClubPlayerResponse> list() {
        return clubPlayerRepository.findAll().stream()
                .map(p -> new ClubPlayerResponse(p.getId(), p.getName(), p.getType(), p.getGender()))
                .toList();
    }
}
