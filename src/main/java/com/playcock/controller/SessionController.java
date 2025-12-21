package com.playcock.controller;

import com.playcock.dto.request.SessionStartRequest;
import com.playcock.dto.response.SessionResponse;
import com.playcock.service.SessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/start")
    public SessionResponse start(@RequestBody SessionStartRequest req) {
        return sessionService.start(req);
    }

    @GetMapping("/current")
    public SessionResponse current() {
        return sessionService.getCurrent();
    }
}
