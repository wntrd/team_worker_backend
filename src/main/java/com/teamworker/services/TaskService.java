package com.teamworker.services;

import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.models.User;

import java.text.ParseException;
import java.util.List;

public interface TaskService {

    Task add(Task task) throws ParseException;

    List<Task> getAll();

    Task update(Long id, Task task) throws ParseException;

    void delete(Long id);

    List<Task> getAllByProject(Project project);

    List<Task> getAllByAssignee(Long id);

    Integer getPercentageOfCompletedOnTime(Long id);

    Integer getNumberByAssigneeAndStage(Long id, String stageName);

    Task getById(Long id);

    List<Task> getAllByStage(String stageName) throws ParseException;

    List<Task> getAllByManager(Long id);

    List<Task> getAllByStageForAdmin(String stageName) throws ParseException;

    List<Task> getAllByStageForManager(String stageName, Long id);

    Task changeStage(Long taskId, String stageName);

    List<Task> getAllByAssigneeAndCreateTime(Long id, String startTime, String finalTime) throws ParseException;
}
