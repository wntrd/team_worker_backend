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

    public Task toTask() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");

        Date parseDueTime = simpleDateFormat.parse(dueTime);
        Timestamp parsedDueTime = new java.sql.Timestamp(parseDueTime.getTime());

        Date parseStartTime = simpleDateFormat.parse(startTime);
        Timestamp parsedStartTime = new java.sql.Timestamp(parseStartTime.getTime());

        Date parseEndTime = simpleDateFormat.parse(endTime);
        Timestamp parsedEndTime = new java.sql.Timestamp(parseEndTime.getTime());

        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setDueTime(parsedDueTime);
        task.setStartTime(parsedStartTime);
        task.setEndTime(parsedEndTime);
        task.setAssignee(assignee.toUser());
        task.setCreator(creator.toUser());
        task.setProject(project);
        task.setPriority(priority);
        task.setStage(stage);
        task.setType(type);
        return task;
    }

    public static TaskDto fromTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setCreateTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(task.getCreateTime()));
        taskDto.setDueTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(task.getDueTime()));
        taskDto.setLastEditTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(task.getLastEditTime()));
        taskDto.setStartTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(task.getStartTime()));
        taskDto.setEndTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(task.getEndTime()));
        taskDto.setAssignee(UserDto.fromUser(task.getAssignee()));
        taskDto.setCreator(UserDto.fromUser(task.getCreator()));
        taskDto.setProject(task.getProject());
        taskDto.setPriority(task.getPriority());
        taskDto.setStage(task.getStage());
        taskDto.setType(task.getType());
        return taskDto;
    }
}
