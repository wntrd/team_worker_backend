package com.teamworker.services;

import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.models.User;

import java.sql.Time;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface TaskService {

    Task add(Task task) throws ParseException;

    List<Task> getAll();

    Task update(Long id, Task task) throws ParseException;

    void delete(Long id);

    List<Task> getAllByProject(Project project);

    List<Task> getAllByAssignee(Long id);

    Integer getPercentageOfCompletedOnTime(Long id);

    Integer getNumberByAssigneeAndStage(Long id, String stageName);

    Integer getNumberByAssigneeAndType(Long id, String typeName);

    Map<String, Integer> getNumbersWithTypesByAssignee(Long id);

    Map<String, Integer> getNumbersWithStagesByAssignee(Long id);

    String getAverageTimeOfCompletingByAssignee(Long id);

    Integer getNumberOfMostProductiveMonthByAssignee(Long id);

    Map<String, Integer> getNumbersWithMonthsByAssignee(Long id);

    Task getTaskWithClosestDueTimeByAssignee(Long id);

    Task getById(Long id);

    List<Task> getAllByStage(Long id, String stageName) throws ParseException;

    List<Task> getAllByManager(User manager);

    List<Task> getAllByStageForAdmin(String stageName) throws ParseException;

    List<Task> getAllByStageForManager(User manager, String stageName);

    Task changeStage(Long taskId, String stageName);

    List<Task> getAllByAssigneeAndCreateTime(Long id, String startTime, String finalTime) throws ParseException;
}
