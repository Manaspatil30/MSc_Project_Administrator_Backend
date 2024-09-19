package com.app.msc_project_administrator.projectAssesment;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.project.Tag;
import com.app.msc_project_administrator.user.Role;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectAssessmentService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectAssessmentRepository projectAssessmentRepository;

    public void assignProjectForAssessment(Long projectId, Long supervisorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Optional<ProjectAssessment> existingAssessment = projectAssessmentRepository.findByProject_ProjectId(projectId);
        if (existingAssessment.isPresent()) {
            throw new RuntimeException("This project is already assigned to a supervisor.");
        }

        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));
        // Check if the user is a supervisor (ACADEMIC role)
        if (!supervisor.getRole().equals(Role.ACADEMIC)) {
            throw new RuntimeException("The assigned user is not a supervisor.");
        }

        ProjectAssessment assessment = new ProjectAssessment();
        assessment.setProject(project);
        assessment.setAssessor(supervisor);
        assessment.setStatus("Pending");

        projectAssessmentRepository.save(assessment);
    }

    public List<User> findSupervisorsByTags(Set<Tag> tags) {
        return userRepository.findByExpertiseTagsIn(tags);
    }
}
