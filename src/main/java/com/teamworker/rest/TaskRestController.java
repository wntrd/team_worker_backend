package com.teamworker.rest;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.ProjectDto;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@CrossOrigin(origins = "http://localhost:4200/")
@Tag(name = "/api/v1/tasks", description = "Контролер для керування завданнями")
public class TaskRestController {

    private final TaskService taskService;

    private final SimpleDateFormat getDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat setDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Додати завдання")
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto taskDto) throws ParseException {

        taskDto.setDueTime(taskDto.getDueTime().replace('T', ' '));
        Timestamp parsedGetDueTime = new Timestamp(getDateFormat.parse(taskDto.getDueTime()).getTime());
        String parsedSetDueTime = setDateFormat.format(parsedGetDueTime.getTime());

        taskDto.setDueTime(parsedSetDueTime);

        Task task = taskService.add(taskDto.toTask());
        if(task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "/get/all")
    @Operation(summary = "Отримати всі завдання")
    public ResponseEntity<List<TaskDto>> getAll() {
        List<Task> tasks = taskService.getAll();
        List<TaskDto> result = tasks.stream().map(TaskDto::fromTask).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/get/all/{stage}")
    @Operation(summary = "Отримати всі завдання за стадією")
    public ResponseEntity<List<TaskDto>> getAllByStage(@PathVariable(value = "stage") String stageName) {
        List<Task> tasks = taskService.getAllByStage(stageName);
        List<TaskDto> result = tasks.stream().map(TaskDto::fromTask).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}/{stage}")
    @Operation(summary = "Оновити стадію виконання завдання")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "stage") String stageName) {

        Task task = taskService.changeStage(id, stageName);

        if(task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Видалити завдання")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable(value = "id") Long id) {
        if(taskService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
