package com.teamworker.services;

import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.models.User;

import java.text.ParseException;
import java.util.List;

public interface TaskService {

    Task add(Task task) throws ParseException;

    List<Task> getAll() throws ParseException;

    Task update(Long id, Task task) throws ParseException;

    void delete(Long id);

    List<Task> getAllByProject(Project project);

    Task getById(Long id);

    List<Task> getAllByStage(String stageName) throws ParseException;

    List<Task> getAllByStageForAdmin(String stageName) throws ParseException;

    Task changeStage(Long taskId, String stageName);

    List<Task> getAllByAssigneeAndCreateTime(Long id, String startTime, String finalTime) throws ParseException;
}
