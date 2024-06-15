package com.teamworker.services;

import com.teamworker.models.Project;
import com.teamworker.models.Task;

import java.util.List;

public interface TaskService {

    Task add(Task task);

    List<Task> getAll();

    Task update(Long id, Task task);

    void delete(Long id);

    List<Task> getAllByProject(Project project);

    Task getById(Long id);
}
