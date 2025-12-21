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
        // ✅ 활성 플레이어만 반환
        return clubPlayerRepository.findByActiveTrueOrderByIdAsc().stream()
                .map(p -> new ClubPlayerResponse(p.getId(), p.getName(), p.getType(), p.getGender()))
                .toList();
    }

    // ✅ 소프트 삭제 (비활성화)
    public void softDelete(Long id) {
        ClubPlayer p = clubPlayerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플레이어입니다. id=" + id));

        // 이미 비활성이라면 멱등 처리
        if (!p.isActive()) return;

        p.deactivate(); // dirty checking으로 UPDATE 됨
    }
}
