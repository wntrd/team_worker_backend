package com.teamworker.rest.admin;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/admin/tasks")
@Tag(name = "/api/v1/admin/tasks", description = "Контролер для адміністрування завдань")
public class TaskAdminRestController {

    private final TaskService taskService;

    @Autowired
    public TaskAdminRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "get/all")
    @Operation(summary = "Отримати всі завдання доступних проектів")
    public ResponseEntity<List<TaskDto>> getAll() {
        List<Task> tasks = taskService.getAll();

        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TaskDto> result = tasks.stream().map(TaskDto::fromTask).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
