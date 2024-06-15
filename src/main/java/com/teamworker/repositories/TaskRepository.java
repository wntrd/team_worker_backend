package com.teamworker.repositories;

import com.teamworker.models.Project;
import com.teamworker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getAllByProject(Project project);
}
