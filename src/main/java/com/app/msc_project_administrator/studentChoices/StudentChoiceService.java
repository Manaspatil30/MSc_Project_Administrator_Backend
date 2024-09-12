package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.project.ProjectService;
import com.app.msc_project_administrator.user.Role;
import com.app.msc_project_administrator.user.SupervisorDTO;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentChoiceService {

    @Autowired
    private StudentChoiceRepository studentChoiceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;


    public void saveStudentChoice(User student, List<StudentChoiceRequest.ProjectPreference> projectPreferences) {
        StudentChoice choice = studentChoiceRepository.findByStudent(student);
        if (choice == null) {
            choice = new StudentChoice();
            choice.setStudent(student);
        }

        // Fetch the projects and set preferences
        List<Project> projects = new ArrayList<>();
        List<Integer> preferences = new ArrayList<>();

        for (StudentChoiceRequest.ProjectPreference projectPreference : projectPreferences) {
            Project project = projectRepository.findById(projectPreference.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            projects.add(project);
            preferences.add(projectPreference.getPreference());
        }

        choice.setProjects(projects);
        choice.setPreferences(preferences);

        // Save the student choice
        studentChoiceRepository.save(choice);
    }

    public void saveStudentChoice2(User student, List<StudentChoiceRequest.ProjectPreference> projectPreferences) {
        // Check if the student has already submitted choices
        StudentChoice choice = studentChoiceRepository.findByStudent(student);

        // Updated: Throw exception if choices are already submitted
        if (choice != null) {
            throw new RuntimeException("Cannot submit preferences again. Choices have already been submitted.");
        }

        // If no previous choices, create a new one
        choice = new StudentChoice();
        choice.setStudent(student);

        // Fetch the projects and set preferences
        List<Project> projects = new ArrayList<>();
        List<Integer> preferences = new ArrayList<>();

        for (StudentChoiceRequest.ProjectPreference projectPreference : projectPreferences) {
            Project project = projectRepository.findById(projectPreference.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            projects.add(project);
            preferences.add(projectPreference.getPreference());
        }

        choice.setProjects(projects);
        choice.setPreferences(preferences);

        // Save the student choice
        studentChoiceRepository.save(choice);
    }

    public StudentChoice getStudentChoice(User student){
        return studentChoiceRepository.findByStudent(student);
    }

    public List<ProjectPreferenceResponse> getStudentProjectPreferences(Long userId) {
        // Fetch the student choices based on the userId
        StudentChoice studentChoice = studentChoiceRepository.findByStudentUserId(userId)
                .orElseThrow(() -> new RuntimeException("No choices found for user: " + userId));

        // Map to ProjectPreferenceResponse
        return studentChoice.getProjects().stream().map(project -> {
            int preferenceIndex = studentChoice.getProjects().indexOf(project);
            return new ProjectPreferenceResponse(
                    Math.toIntExact(userId),
                    project.getProjectId(),
                    project.getTitle(),
                    studentChoice.getPreferences().get(preferenceIndex)
            );
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllStudentProjectPreferences() {
        // Fetch all student choices
        List<StudentChoice> allChoices = studentChoiceRepository.findAll();

        // Prepare the response format
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (StudentChoice choice : allChoices) {
            Map<String, Object> studentPreferenceMap = new HashMap<>();
            studentPreferenceMap.put("studentId", choice.getStudent().getUserId());

            List<Map<String, Object>> preferencesList = new ArrayList<>();
            for (int i = 0; i < choice.getProjects().size(); i++) {
                Project project = choice.getProjects().get(i);
                Map<String, Object> projectMap = new HashMap<>();
                projectMap.put("projectId", project.getProjectId());
                projectMap.put("projectTitle", project.getTitle());
                projectMap.put("preference", choice.getPreferences().get(i));
                preferencesList.add(projectMap);
            }

            studentPreferenceMap.put("preferences", preferencesList);
            responseList.add(studentPreferenceMap);
        }

        return responseList;
    }

//    public Object getProjectOrChoices(Long studentId) {
//        ProjectDTO assignedProject = projectService.getAssignedProject(studentId);
//        if (assignedProject != null) {
//            // Map to ProjectDTO and return
//            SupervisorDTO supervisorDTO = new SupervisorDTO(
//                    assignedProject.getSupervisor().getUserId(),
//                    assignedProject.getSupervisor().getFirstname(),
//                    assignedProject.getSupervisor().getLastname(),
//                    assignedProject.getSupervisor().getEmail()
//            );
//
//            ProjectDTO projectDTO = new ProjectDTO(
//                    assignedProject.getProjectId(),
//                    assignedProject.getSupProjectId(),
//                    assignedProject.getTitle(),
//                    assignedProject.getDescription(),
//                    assignedProject.getStatus(),
//                    supervisorDTO
//            );
//
//            return projectDTO; // Return the allocated project
//        } else {
//            // If no project is assigned, return the student's choices
//            List<ProjectPreferenceResponse> choices = getStudentProjectPreferences(studentId);
//            return choices; // Return the list of choices
//        }
//    }

    public Object getProjectOrChoices(Long studentId) {
        try {
            ProjectDTO assignedProject = projectService.getAssignedProject(studentId);
            if (assignedProject != null) {
                // Map to ProjectDTO and return
                SupervisorDTO supervisorDTO = new SupervisorDTO(
                        assignedProject.getSupervisor().getUserId(),
                        assignedProject.getSupervisor().getFirstname(),
                        assignedProject.getSupervisor().getLastname(),
                        assignedProject.getSupervisor().getEmail()
                );

                ProjectDTO projectDTO = new ProjectDTO(
                        assignedProject.getProjectId(),
                        assignedProject.getSupProjectId(),
                        assignedProject.getTitle(),
                        assignedProject.getDescription(),
                        assignedProject.getStatus(),
                        supervisorDTO,
                        assignedProject.getProgrames()
                );

                return projectDTO; // Return the allocated project
            }
        } catch (RuntimeException e) {
            // No project assigned, return choices or a message
        }

        // If no project is assigned, return the student's choices or a message
        List<ProjectPreferenceResponse> choices = getStudentProjectPreferences(studentId);
        if (choices == null || choices.isEmpty()) {
            return "No project assigned or choices selected.";
        } else {
            return choices; // Return the list of choices
        }
    }

    /**
     * Fetch all student preferences and students who haven't submitted their preferences.
     */
    public Map<String, Object> getStudentPreferencesAndMissingSubmissions() {
        // Fetch all students with the role STUDENT
        List<User> allStudents = userRepository.findByRole(Role.STUDENT);

        // Fetch all students who have submitted their preferences
        List<StudentChoice> studentChoices = studentChoiceRepository.findAll();

        // Extract students who have submitted preferences
        List<User> studentsWithPreferences = studentChoices.stream()
                .map(StudentChoice::getStudent)
                .collect(Collectors.toList());

        // Find students who haven't submitted preferences
        List<User> studentsWithoutPreferences = allStudents.stream()
                .filter(student -> !studentsWithPreferences.contains(student))
                .collect(Collectors.toList());

        // Prepare the response with projects and preferences combined
        List<Map<String, Object>> choicesWithPreferences = studentChoices.stream()
                .map(choice -> {
                    Map<String, Object> choiceMap = new HashMap<>();
                    choiceMap.put("student", choice.getStudent());

                    // Combine projects with corresponding preferences
                    List<Map<String, Object>> projectPreferences = new ArrayList<>();
                    for (int i = 0; i < choice.getProjects().size(); i++) {
                        Project project = choice.getProjects().get(i);
                        int preference = choice.getPreferences().get(i);

                        Map<String, Object> projectPrefMap = new HashMap<>();
                        projectPrefMap.put("projectId", project.getProjectId());
                        projectPrefMap.put("projectTitle", project.getTitle());
                        projectPrefMap.put("preference", preference);

                        projectPreferences.add(projectPrefMap);
                    }
                    choiceMap.put("projectPreferences", projectPreferences);

                    return choiceMap;
                })
                .collect(Collectors.toList());

        // Prepare the final response
        Map<String, Object> response = new HashMap<>();
        response.put("studentChoices", choicesWithPreferences); // All student preferences with project info
        response.put("studentsWithoutPreferences", studentsWithoutPreferences); // Students who haven't submitted preferences
        response.put("remainingCount", studentsWithoutPreferences.size()); // Count of students who haven't submitted

        return response;
    }
}
