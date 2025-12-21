package com.playcock.controller;

import com.playcock.dto.request.MatchStartRequest;
import com.playcock.dto.request.SessionParticipantsUpdateRequest;
import com.playcock.dto.request.SessionStartRequest;
import com.playcock.dto.request.WaitingTeamCreateRequest;
import com.playcock.dto.response.DashboardResponse;
import com.playcock.service.SessionFlowService;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionFlowController {

    private final SessionFlowService service;

    public SessionFlowController(SessionFlowService service) {
        this.service = service;
    }

    // 세션 시작
    @PostMapping("/sessions/start")
    public DashboardResponse start(@RequestBody SessionStartRequest req) {
        return service.startSession(req);
    }

    // 현재 세션 대시보드(한 번에 LIST/WAITING/PLAYING)
    @GetMapping("/sessions/current/dashboard")
    public DashboardResponse dashboard() {
        return service.dashboard();
    }

    // 세션 종료
    @PostMapping("/sessions/current/end")
    public void end() {
        service.endSession();
    }

    // WAITING 팀 생성 (LIST에서 1~4명 선택)
    @PostMapping("/waiting-teams")
    public DashboardResponse createWaitingTeam(@RequestBody WaitingTeamCreateRequest req) {
        return service.createWaitingTeam(req);
    }

    // WAITING 팀 삭제(해체 -> LIST 복귀)
    @DeleteMapping("/waiting-teams/{teamId}")
    public DashboardResponse deleteWaitingTeam(@PathVariable Long teamId) {
        return service.deleteWaitingTeam(teamId);
    }

    // 경기 시작 (WAITING 팀 -> PLAYING)
    @PostMapping("/matches/start")
    public DashboardResponse startMatch(@RequestBody MatchStartRequest req) {
        return service.startMatch(req);
    }

    // 경기 종료 (PLAYING -> LIST 복귀 + 휴식시간 기준 갱신)
    @PostMapping("/matches/{matchId}/end")
    public DashboardResponse endMatch(@PathVariable Long matchId) {
        return service.endMatch(matchId);
    }

    @PostMapping("/sessions/current/participants")
    public DashboardResponse updateParticipants(@RequestBody SessionParticipantsUpdateRequest req) {
        return service.updateParticipants(req);
    }
}
