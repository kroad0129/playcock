package com.playcock.controller;

import com.playcock.dto.request.ClubPlayerCreateRequest;
import com.playcock.dto.response.ClubPlayerResponse;
import com.playcock.service.ClubPlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club-players")
public class ClubPlayerController {

    private final ClubPlayerService clubPlayerService;

    public ClubPlayerController(ClubPlayerService clubPlayerService) {
        this.clubPlayerService = clubPlayerService;
    }

    @PostMapping
    public ClubPlayerResponse create(@RequestBody ClubPlayerCreateRequest req) {
        return clubPlayerService.create(req);
    }

    @GetMapping
    public List<ClubPlayerResponse> list() {
        return clubPlayerService.list();
    }

    // ✅ 소프트 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clubPlayerService.softDelete(id);
    }
}
