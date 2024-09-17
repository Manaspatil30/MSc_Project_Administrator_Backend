package com.app.msc_project_administrator.supervisorStudentPreference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupervisorStudentPreferenceRepository extends JpaRepository<SupervisorStudentPreference, Long> {
    // Adjusted query to find by project ID
    List<SupervisorStudentPreference> findByProject_ProjectId(Integer projectId);
}
