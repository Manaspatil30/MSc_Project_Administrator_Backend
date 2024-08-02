package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentChoiceService {

    @Autowired
    private StudentChoiceRepository studentChoiceRepository;

    @Autowired
    private ProjectRepository projectRepository;

//    public void saveStudentChoice(User student, List<Long> projectIds){
//        StudentChoice choice = studentChoiceRepository.findByStudent(student);
//        if(choice == null) {
//            choice = new StudentChoice();
//            choice.setStudent(student);
//        }
//        List<Project> projects = projectRepository.findAllById(projectIds);
//        choice.setProjects(projects);
//        studentChoiceRepository.save(choice);
//    }

    public StudentChoice getStudentChoice(User student){
        return studentChoiceRepository.findByStudent(student);
    }
}
