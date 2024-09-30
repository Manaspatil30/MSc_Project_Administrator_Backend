package com.app.msc_project_administrator.user;

import com.app.msc_project_administrator.answer.AnswerDTO;
import com.app.msc_project_administrator.answer.UserWithAnswersDTO;
import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.studentChoices.StudentChoice;
import com.app.msc_project_administrator.studentChoices.StudentChoiceRepository;
import com.app.msc_project_administrator.supervisorStudentPreference.ProjectWithRankedStudentsDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.RankedStudentDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreference;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreferenceRepository;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignment;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserProjectAssignmentRepository userProjectAssignmentRepository;
    private final ProjectRepository projectRepository;
    private final StudentChoiceRepository studentChoiceRepository;
    private final SupervisorStudentPreferenceRepository preferenceRepository;

    public List<User> findAll(){
        return repository.findAll();
    }

    public List<User> getUsersByIds(List<Integer> ids){
        return repository.findAllByUserIdIn(ids);
    }

    public List<User> getAllSupervisors(Role role) {return repository.findByRole(role);}

    public List<UserDTO> getStudentsBySupervisor(Long supervisorId) {
        List<User> students = repository.findStudentsBySupervisorId(supervisorId);
        return students.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }



    public List<UserDTO> getStudentsAndAssignedProjects(Long supervisorId) {
        List<UserProjectAssignment> assignments = userProjectAssignmentRepository.findBySupervisorId(supervisorId);

        return assignments.stream().map(assignment -> {
            User student = assignment.getUser();
            Project project = assignment.getProject();

            // Map to UserDTO and include project details
            UserDTO studentDTO = mapToUserDTO(student);
            ProjectDTO projectDTO = mapToProjectDTO(project);
            studentDTO.setAssignedProject(projectDTO);

            return studentDTO;
        }).collect(Collectors.toList());
    }

    private ProjectDTO mapToProjectDTO(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getSupProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                null,
                project.getPrograme(),
                project.getQuestions(),
                project.getQuota()
        );
    }

    public List<ProjectWithStudentsDTO> getStudentsForSupervisor(Long supervisorId) {
        // Fetch projects by supervisor
        List<Project> supervisorProjects = projectRepository.findAllBySupervisorUserId(supervisorId);

        // Create a list to store the result
        List<ProjectWithStudentsDTO> projectWithStudentsList = new ArrayList<>();

        for (Project project : supervisorProjects) {
            // Check if preferences have already been submitted for this project
            List<SupervisorStudentPreference> existingPreferences = preferenceRepository.findByProject_ProjectId(project.getProjectId());

            // Skip projects that already have submitted preferences
            if (!existingPreferences.isEmpty()) {
                continue;
            }

            List<StudentChoice> choices = studentChoiceRepository.findByProjectsProjectId(project.getProjectId());

            // Collect students who chose this project, along with their answers
            List<UserWithAnswersDTO> studentDTOs = choices.stream()
                    .map(choice -> {
                        UserWithAnswersDTO userWithAnswersDTO = new UserWithAnswersDTO();
                        userWithAnswersDTO.setUserId(choice.getStudent().getUserId());
                        userWithAnswersDTO.setFirstname(choice.getStudent().getFirstname());
                        userWithAnswersDTO.setLastname(choice.getStudent().getLastname());
                        userWithAnswersDTO.setEmail(choice.getStudent().getEmail());

                        // Fetch and map the answers for this project
                        List<AnswerDTO> answerDTOs = choice.getAnswers().stream()
                                .map(answer -> {
                                    AnswerDTO answerDTO = new AnswerDTO();
                                    answerDTO.setQuestionId(Long.valueOf(answer.getQuestion().getQuestionId()));
                                    answerDTO.setQuestionText(answer.getQuestion().getQuestionText());
                                    answerDTO.setAnswer(answer.getAnswer());
                                    return answerDTO;
                                })
                                .collect(Collectors.toList());

                        userWithAnswersDTO.setAnswers(answerDTOs);
                        return userWithAnswersDTO;
                    })
                    .collect(Collectors.toList());

            // Create a new DTO for the project with students
            ProjectWithStudentsDTO projectWithStudentsDTO = new ProjectWithStudentsDTO();
            projectWithStudentsDTO.setProjectId(project.getProjectId());
            projectWithStudentsDTO.setSupProjectId(project.getSupProjectId());
            projectWithStudentsDTO.setTitle(project.getTitle());
            projectWithStudentsDTO.setDescription(project.getDescription());
            projectWithStudentsDTO.setStatus(project.getStatus());
            projectWithStudentsDTO.setStudents(studentDTOs);

            // Add to the list
            projectWithStudentsList.add(projectWithStudentsDTO);
        }

        return projectWithStudentsList;
    }

    public List<ProjectWithRankedStudentsDTO> getProjectsWithRankedStudents() {
        List<ProjectWithRankedStudentsDTO> projectWithRankedStudents = new ArrayList<>();

        // Fetch all projects
        List<Project> allProjects = projectRepository.findAll();

        for (Project project : allProjects) {
            // Check if preferences have been submitted for this project
            List<SupervisorStudentPreference> rankedStudents = preferenceRepository.findByProject_ProjectId(project.getProjectId());

            // Skip projects without preferences
            if (rankedStudents.isEmpty()) {
                continue;
            }

            ProjectWithRankedStudentsDTO projectDTO = new ProjectWithRankedStudentsDTO();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectTitle(project.getTitle());


            List<RankedStudentDTO> rankedStudentDTOs = rankedStudents.stream()
                    .map(preference -> {
                        User student = preference.getStudent();
                        return new RankedStudentDTO(
                                student.getUserId(),
                                student.getFirstname(),
                                student.getLastname(),
                                student.getEmail(),
                                preference.getPreference()
                        );
                    })
                    .collect(Collectors.toList());

            projectDTO.setRankedStudents(rankedStudentDTOs);
            projectWithRankedStudents.add(projectDTO);
        }

        return projectWithRankedStudents;
    }

    public UserDTO mapToUserDTO(User user) {

        Optional<UserProjectAssignment> assignment = userProjectAssignmentRepository.findByUser_UserId(user.getUserId());

        ProjectDTO projectDTO = null;
        if (assignment.isPresent()) {
            Project assignedProject = assignment.get().getProject();

        SupervisorDTO supervisorDTO = null;
        if (assignedProject.getSupervisor() != null) {
            supervisorDTO = new SupervisorDTO(
                    assignedProject.getSupervisor().getUserId(),
                    assignedProject.getSupervisor().getFirstname(),
                    assignedProject.getSupervisor().getLastname(),
                    assignedProject.getSupervisor().getEmail()
            );
        }


            projectDTO = new ProjectDTO(
                    assignedProject.getProjectId(),
                    assignedProject.getSupProjectId(),
                    assignedProject.getTitle(),
                    assignedProject.getDescription(),
                    assignedProject.getStatus(),
                    supervisorDTO,
                    assignedProject.getPrograme(),
                    assignedProject.getQuestions(),
                    assignedProject.getQuota()
            );
        }

        return new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                projectDTO
        );
    }

}
