package com.app.msc_project_administrator.tasterSessions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasterSessionRepository extends JpaRepository<TasterSession, Long> {
    List<TasterSession> findBySupervisorUserId(Long supervisorId);
    List<TasterSession> findByStudentUserId(Long studentId);

    List<TasterSession> findByStudent_UserId(Long studentId);
    List<TasterSession> findBySupervisor_UserId(Long supervisorId);
}
