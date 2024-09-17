package com.app.msc_project_administrator.supervisorStudentPreference;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorStudentPreferenceService {

    @Autowired
    private SupervisorStudentPreferenceRepository repository;

    public void savePreferences(Long supervisorId, Integer projectId, List<SupervisorStudentPreferenceDTO> preferences) {
        for (SupervisorStudentPreferenceDTO preferenceDTO : preferences) {
            // Create a new SupervisorStudentPreference entity
            SupervisorStudentPreference preference = new SupervisorStudentPreference();

            // Set the supervisor
            User supervisor = new User();
            supervisor.setUserId(supervisorId);
            preference.setSupervisor(supervisor);

            // Set the student
            User student = new User();
            student.setUserId(preferenceDTO.getStudentId());
            preference.setStudent(student);

            // Set the project
            Project project = new Project();
            project.setProjectId(projectId);
            preference.setProject(project);

            // Set the ranking
            preference.setPreference(preferenceDTO.getPreference());

            repository.save(preference);
        }
    }

}

