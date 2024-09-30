package com.app.msc_project_administrator.tasterSessions;

import com.app.msc_project_administrator.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasterSessionService {

    @Autowired
    private TasterSessionRepository tasterSessionRepository;

    @Autowired
    private TasterSessionRequestRepository tasterSessionRequestRepository;

    public void requestTasterSession(Long studentId, Long supervisorId) {
        TasterSessionRequest request = new TasterSessionRequest();
        request.setStudent(new User(studentId));
        request.setSupervisor(new User(supervisorId));
        request.setStatus(TasterSessionStatus.PENDING);
        tasterSessionRequestRepository.save(request);
    }

    public void createTasterSession(Long supervisorId, Long studentId, String meetingLink, String description, LocalDateTime startTime) {
        // Validate the taster session request
        TasterSessionRequest request = tasterSessionRequestRepository
                .findByStudentUserId(studentId)
                .stream()
                .filter(req -> req.getSupervisor().getUserId().equals(supervisorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Taster session request not found"));

        // Create the taster session
        TasterSession session = new TasterSession();
        session.setSupervisor(new User(supervisorId));
        session.setStudent(new User(studentId));
        session.setMeetingLink(meetingLink);
        session.setDescription(description);
        session.setStartTime(startTime);
        session.setStatus(TasterSessionStatus.BOOKED);
        tasterSessionRepository.save(session);

        // Update request status
        request.setStatus(TasterSessionStatus.APPROVED);
        tasterSessionRequestRepository.save(request);

    }

    public List<TasterSessionDTO> getTasterSessionsForStudent(Long studentId) {
        List<TasterSession> sessions = tasterSessionRepository.findByStudent_UserId(studentId);
        return sessions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TasterSessionDTO> getTasterSessionsForSupervisor(Long supervisorId) {
        List<TasterSession> sessions = tasterSessionRepository.findBySupervisor_UserId(supervisorId);
        return sessions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TasterSessionRequestDTO> getTasterSessionRequestsForSupervisors(Long supervisorId){
        List<TasterSessionRequest> requests = tasterSessionRequestRepository.findBySupervisorUserId(supervisorId);
        return requests.stream().map(this::convertToRequestDTO).collect(Collectors.toList());
    }

    private TasterSessionDTO convertToDTO(TasterSession session) {
        TasterSessionDTO dto = new TasterSessionDTO();
        dto.setSessionId(session.getTasterSessionId());
        dto.setSupervisorId(session.getSupervisor().getUserId());
        dto.setSupervisorName(session.getSupervisor().getFirstname() + " " + session.getSupervisor().getLastname());
        dto.setStudentId(session.getStudent() != null ? session.getStudent().getUserId() : null);
        dto.setStudentName(session.getStudent() != null ? session.getStudent().getFirstname() + " " + session.getStudent().getLastname() : "Unassigned");
        dto.setMeetingLink(session.getMeetingLink());
        dto.setDescription(session.getDescription());
        dto.setStartTime(session.getStartTime());
        dto.setStatus(session.getStatus());
        return dto;
    }

    private TasterSessionRequestDTO convertToRequestDTO(TasterSessionRequest request){
        TasterSessionRequestDTO dto = new TasterSessionRequestDTO();
        dto.setStudentId(request.getStudent().getUserId());
        dto.setSupervisorId(request.getSupervisor().getUserId());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
