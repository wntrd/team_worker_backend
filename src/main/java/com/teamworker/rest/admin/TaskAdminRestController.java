package com.teamworker.rest.admin;

import com.teamworker.dtos.ProjectDto;
import com.teamworker.dtos.StatisticsDto;
import com.teamworker.dtos.TaskDto;
import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/admin/tasks")
@Tag(name = "/api/v1/admin/tasks", description = "Контролер для адміністрування завдань")
public class TaskAdminRestController {

    private final TaskService taskService;
    private final SimpleDateFormat getDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat setDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    private final SimpleDateFormat statisticsDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    public TaskAdminRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "get/all")
    @Operation(summary = "Отримати всі завдання доступних проектів")
    public ResponseEntity<List<TaskDto>> getAll() throws ParseException {

        List<Task> tasks = taskService.getAll();

        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TaskDto> result = new ArrayList<>();

        for (Task task : tasks) {
            result.add(TaskDto.fromTask(task));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Оновити завдання")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(value = "id") Long id,
            @RequestBody TaskDto taskDto) throws ParseException {

        Task task = taskService.update(id, taskDto.toTask());

        if(task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/get/all/{stage}")
    @Operation(summary = "Отримати всі завдання за стадією для адміністратора")
    public ResponseEntity<List<TaskDto>> getAllByStage(@PathVariable(value = "stage") String stageName) throws ParseException {
        List<Task> tasks = taskService.getAllByStageForAdmin(stageName);
        List<TaskDto> result = new ArrayList<>();

        for (Task task : tasks) {
            result.add(TaskDto.fromTask(task));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/get/all/{id}")
    @Operation(summary = "Отримати всі завдання працівника за певний період")
    public ResponseEntity<List<TaskDto>> getAllByAssigneeAndCreateTime(@PathVariable(value = "id") Long id,
                                                                       @RequestBody StatisticsDto statisticsDto) throws ParseException {
        List<Task> tasks = taskService.getAllByAssigneeAndCreateTime(id, statisticsDto.getTime1(), statisticsDto.getTime2());
        List<TaskDto> result = new ArrayList<>();

        for (Task task : tasks) {
            result.add(TaskDto.fromTask(task));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
