package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findProjectByProjectId(Long projectId);

    List<Project> findAllBySupervisor(User supervisor);

    long countBySupervisor(User supervisor);
}
