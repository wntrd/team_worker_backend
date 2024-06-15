package com.teamworker.repositories;

import com.teamworker.models.Project;
import com.teamworker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> getAllByManagerId(Long id);
}
