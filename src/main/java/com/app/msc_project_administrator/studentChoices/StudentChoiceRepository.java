package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentChoiceRepository extends JpaRepository<StudentChoice, Long> {
    StudentChoice findByStudent(User student);
}
