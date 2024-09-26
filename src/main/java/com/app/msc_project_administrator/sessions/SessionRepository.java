package com.app.msc_project_administrator.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findBySupervisor_UserId(Long supervisorId);
    List<Session> findByStudent_UserId(Long studentId);
}