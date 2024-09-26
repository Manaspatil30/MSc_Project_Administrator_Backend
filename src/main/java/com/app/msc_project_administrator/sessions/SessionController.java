package com.app.msc_project_administrator.sessions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<Session> createSession(@RequestBody SessionRequest request) {
        Session session = sessionService.createSession(
                request.getSupervisorId(),
                request.getStudentId(),
                request.getTitle(),
                request.getDateTime(),
                request.getMeetingLink()
        );
        return ResponseEntity.ok(session);
    }

    @GetMapping("/supervisor/{supervisorId}")
    public ResponseEntity<List<Session>> getSessionsForSupervisor(@PathVariable Long supervisorId) {
        return ResponseEntity.ok(sessionService.getSessionsForSupervisor(supervisorId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Session>> getSessionsForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(sessionService.getSessionsForStudent(studentId));
    }

    @PutMapping("/update/{sessionId}")
    public ResponseEntity<Session> updateSession(@PathVariable Long sessionId, @RequestBody UpdateSessionRequest request) {
        Session updatedSession = sessionService.updateSession(sessionId, request.getTitle(), request.getDateTime(), request.getMeetingLink());
        return ResponseEntity.ok(updatedSession);
    }
}
