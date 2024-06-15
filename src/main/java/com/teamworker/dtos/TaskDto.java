package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.models.enums.Priority;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.models.enums.TaskType;
import lombok.Data;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private String createTime;
    private String dueTime;
    private String lastEditTime;
    private String startTime;
    private String endTime;
    private UserDto assignee;
    private UserDto creator;
    private Project project;
    private Priority priority;
    private TaskStage stage;
    private TaskType type;
    private boolean isOverdue;

    public Task toTask() {

        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setCreateTime(createTime);
        task.setDueTime(dueTime);
        task.setLastEditTime(lastEditTime);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setAssignee(assignee.toUser());
        task.setCreator(creator.toUser());
        task.setProject(project);
        task.setPriority(priority);
        task.setStage(stage);
        task.setType(type);
        task.setOverdue(isOverdue);
        return task;
    }

    public static TaskDto fromTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setCreateTime(task.getCreateTime());
        taskDto.setDueTime(task.getDueTime());
        taskDto.setLastEditTime(task.getLastEditTime());
        taskDto.setStartTime(task.getStartTime());
        taskDto.setEndTime(task.getEndTime());
        taskDto.setAssignee(UserDto.fromUser(task.getAssignee()));
        taskDto.setCreator(UserDto.fromUser(task.getCreator()));
        taskDto.setProject(task.getProject());
        taskDto.setPriority(task.getPriority());
        taskDto.setStage(task.getStage());
        taskDto.setType(task.getType());
        taskDto.setOverdue(task.isOverdue());
        return taskDto;
    }
}
