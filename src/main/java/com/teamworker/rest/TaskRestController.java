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

import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@Tag(name = "/api/v1/tasks", description = "Контролер для керування завданнями")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Додати завдання")
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto taskDto) throws ParseException {
        Task task = taskService.add(taskDto.toTask());
        if(task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
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
