package com.app.msc_project_administrator.userProjectAssign;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.project.ProjectService;
import com.app.msc_project_administrator.projectAssesment.AssignmentRequest;
import com.app.msc_project_administrator.studentChoices.ProjectPreferenceResponse;
import com.app.msc_project_administrator.studentChoices.StudentChoice;
import com.app.msc_project_administrator.studentChoices.StudentChoiceRepository;
import com.app.msc_project_administrator.studentChoices.StudentChoiceService;
import com.app.msc_project_administrator.supervisorStudentPreference.ProjectWithRankedStudentsDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.RankedStudentDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreference;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreferenceRepository;
import com.app.msc_project_administrator.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectAssignmentService {

    private final StudentChoiceService studentChoiceService;
    private final StudentChoiceRepository studentChoiceRepository;
    private final SupervisorStudentPreferenceRepository preferenceRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectAssignmentRepository userProjectAssignmentRepository;
    private final ProjectService projectService;

    public void allocateProjects() {
        // Step 1: Retrieve all student preferences
        List<Map<String, Object>> allStudentChoices = studentChoiceService.getAllStudentProjectPreferences();

        // Retrieve all projects and supervisor preferences
        List<Project> allProjects = projectRepository.findAll();

        Map<Integer, Project> projectMap = allProjects.stream()
                .collect(Collectors.toMap(Project::getProjectId, p -> p));

        // Step 2: Sort students by preferences
        List<StudentChoice> studentChoices = studentChoiceRepository.findAll();
        List<UserProjectAssignment> assignments = new ArrayList<>();

        // Initialize a map to track project allocations
        Map<Long, Integer> projectQuotaMap = new HashMap<>();
        for (Project project : allProjects) {
            projectQuotaMap.put(project.getProjectId().longValue(), project.getQuota());
        }

        // Step 3: Allocate projects based on preferences
        for (StudentChoice choice : studentChoices) {
            User student = choice.getStudent();
            List<Project> preferredProjects = choice.getProjects();

            // Attempt to allocate the highest available preference
            for (int i = 0; i < preferredProjects.size(); i++) {
                Project preferredProject = preferredProjects.get(i);
                Integer remainingQuota = projectQuotaMap.get(preferredProject.getProjectId().longValue());

                // Check if there's available quota and the supervisor prefers the student
                if (remainingQuota > 0) {
                    // Allocate the project
                    projectService.assignProjectToStudent(student.getUserId(), preferredProject.getProjectId().longValue());

                    // Update the quota
                    projectQuotaMap.put(preferredProject.getProjectId().longValue(), remainingQuota - 1);

                    // Delete student's preferences after allocation
                    //studentChoiceRepository.delete(choice);

                    // Remove this student from supervisor's preference list if you have a supervisor-student preference table
                    //preferenceRepository.deleteByStudentIdAndProjectId(student.getUserId(), preferredProject.getProjectId());

                    break; // Exit the loop after successful assignment
                }
            }
        }
    }

}
