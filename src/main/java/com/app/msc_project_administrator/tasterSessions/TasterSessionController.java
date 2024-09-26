package com.app.msc_project_administrator.tasterSessions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/taster-sessions")
public class TasterSessionController {

    @Autowired
    private TasterSessionService tasterSessionService;

    @PostMapping("/request")
    public ResponseEntity<String> requestTasterSession(@RequestBody TasterSessionRequestDTO requestDTO) {
        tasterSessionService.requestTasterSession(requestDTO.getStudentId(), requestDTO.getSupervisorId());
        return ResponseEntity.ok("Taster session requested.");
    }

    @PostMapping("/supervisor/{supervisorId}/create")
    public ResponseEntity<String> createTasterSession(
            @PathVariable Long supervisorId,
            @RequestParam Long studentId,
            @RequestParam String meetingLink,
            @RequestParam String description,
            @RequestParam String startTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        tasterSessionService.createTasterSession(supervisorId, studentId, meetingLink, description, start);
        return ResponseEntity.ok("Taster session created.");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TasterSessionDTO>> getStudentTasterSessions(@PathVariable Long studentId) {
        return ResponseEntity.ok(tasterSessionService.getTasterSessionsForStudent(studentId));
    }

    @GetMapping("/requests/{supervisorId}")
    public  ResponseEntity<List<TasterSessionRequestDTO>> getRequestsBySupervisor(@PathVariable Long supervisorId){
        return ResponseEntity.ok(tasterSessionService.getTasterSessionRequestsForSupervisors(supervisorId));
    }

    @GetMapping("/supervisor/{supervisorId}")
    public ResponseEntity<List<TasterSessionDTO>> getSupervisorTasterSessions(@PathVariable Long supervisorId) {
        return ResponseEntity.ok(tasterSessionService.getTasterSessionsForSupervisor(supervisorId));
    }
}
