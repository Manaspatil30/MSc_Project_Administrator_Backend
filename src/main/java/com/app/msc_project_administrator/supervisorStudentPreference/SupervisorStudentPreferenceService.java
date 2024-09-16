package com.app.msc_project_administrator.supervisorStudentPreference;

import com.app.msc_project_administrator.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorStudentPreferenceService {

    @Autowired
    private SupervisorStudentPreferenceRepository repository;

    public void savePreferences(Long supervisorId, List<SupervisorStudentPreference> preferences) {
        for (SupervisorStudentPreference preference : preferences) {
            // Ensure the preference is linked to the correct supervisor
            User supervisor = new User();
            supervisor.setUserId(supervisorId);
            preference.setSupervisor(supervisor);
            repository.save(preference);
        }
    }
}

