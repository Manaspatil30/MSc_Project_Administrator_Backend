package com.app.msc_project_administrator.projectAssesment;
import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAssessmentRepository extends JpaRepository<ProjectAssessment, Long> {
    List<ProjectAssessment> findBySupervisor(User supervisor);
}
