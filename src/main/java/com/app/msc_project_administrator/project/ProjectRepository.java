package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findProjectByProjectId(Long projectId);

    List<Project> findAllBySupervisor(User supervisor);

    long countBySupervisor(User supervisor);

    @Query("SELECT p FROM Project p " +
            "JOIN p.programe programs " +
            "JOIN p.tags tags " +
            "JOIN p.supervisor supervisor " +
            "WHERE (:programIds IS NULL OR programs.id IN :programIds) " +
            "AND (:tagNames IS NULL OR tags.name IN :tagNames) " +
            "AND (:supervisorName IS NULL OR CONCAT(supervisor.firstname, ' ', supervisor.lastname) LIKE %:supervisorName%) " +
            "AND (:title IS NULL OR p.title LIKE %:title%) " +
            "ORDER BY p.supProjectId ASC")
    List<Project> filterProjects(
            @Param("programIds") List<Long> programIds,
            @Param("tagNames") List<String> tagNames,
            @Param("supervisorName") String supervisorName,
            @Param("title") String title);

    //Filter
    @Query("SELECT p FROM Project p JOIN p.programe pr WHERE pr.programId = :programId")
    List<Project> findByPrograme(Long programId);

    @Query("SELECT p FROM Project p JOIN p.tags t WHERE t.name = :tagName")
    List<Project> findByTag(String tagName);

    List<Project> findBySupervisor(User supervisor);

    List<Project> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT p FROM Project p JOIN p.supervisor s JOIN p.tags t WHERE p.title LIKE %:query% OR s.firstname LIKE %:query% OR s.lastname LIKE %:query% OR t.name LIKE %:query%")
    List<Project> searchByTitleSupervisorOrTags(String query);

    // Find all projects sorted by supProjectId (by default ascending)
    @Query("SELECT p FROM Project p ORDER BY p.supProjectId ASC")
    List<Project> findAllSortedBySupProjectId();

}
