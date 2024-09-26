package com.app.msc_project_administrator.sessions;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public Session createSession(Long supervisorId, Long studentId, String title, LocalDateTime dateTime, String meetingLink) {
        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Session session = new Session();
        session.setSupervisor(supervisor);
        session.setStudent(student);
        session.setTitle(title);
        session.setDateTime(dateTime);
        session.setMeetingLink(meetingLink);
        session.setStatus(SessionStatus.PENDING);

        return sessionRepository.save(session);
    }

    public List<Session> getSessionsForSupervisor(Long supervisorId) {
        return sessionRepository.findBySupervisor_UserId(supervisorId);
    }

    public List<Session> getSessionsForStudent(Long studentId) {
        return sessionRepository.findByStudent_UserId(studentId);
    }

    public Session updateSession(Long sessionId, String title, LocalDateTime dateTime, String meetingLink) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setTitle(title);
        session.setDateTime(dateTime);
        session.setMeetingLink(meetingLink);

        return sessionRepository.save(session);
    }
}
