package com.app.msc_project_administrator.tasterSessions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasterSessionRequestRepository extends JpaRepository<TasterSessionRequest, Long> {
    List<TasterSessionRequest> findBySupervisorUserId(Long supervisorId);

    List<TasterSessionRequest> findByStudentUserId(Long studentId);
}
