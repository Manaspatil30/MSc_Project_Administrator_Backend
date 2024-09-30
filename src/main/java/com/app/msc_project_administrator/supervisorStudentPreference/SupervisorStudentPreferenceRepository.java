package com.app.msc_project_administrator.supervisorStudentPreference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupervisorStudentPreferenceRepository extends JpaRepository<SupervisorStudentPreference, Long> {
    List<SupervisorStudentPreference> findByProject_ProjectId(Integer projectId);

    void deleteByStudent_UserIdAndProject_ProjectId(Long studentId, Integer projectId);
}
