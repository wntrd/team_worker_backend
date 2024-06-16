package com.teamworker.rest;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.ProjectDto;
import com.teamworker.dtos.TaskDto;
import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@CrossOrigin(origins = "http://localhost:4200/")
@Tag(name = "/api/v1/tasks", description = "Контролер для керування завданнями")
public class TaskRestController {

    private final TaskService taskService;
    private final UserService userService;

    private final SimpleDateFormat getDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat setDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    @Autowired
    public TaskRestController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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

    @GetMapping(value = "/get/all/{stage}")
    @Operation(summary = "Отримати всі завдання за стадією")
    public ResponseEntity<List<TaskDto>> getAllByStage(@PathVariable(value = "stage") String stageName) throws ParseException {
        List<Task> tasks = taskService.getAllByStage(stageName);
        List<TaskDto> result = new ArrayList<>();

        for (Task task : tasks) {
            result.add(TaskDto.fromTask(task));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}/{stage}")
    @Operation(summary = "Оновити стадію виконання завдання")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "stage") String stageName) throws ParseException {

        Task task = taskService.changeStage(id, stageName);

        if(task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/ontime")
    @Operation(summary = "Отримати відсоток зроблених вчасно завдань авторизованого користувача")
    public ResponseEntity<Integer> getPercentageOfMadeOnTime() {
        Integer percentage = taskService.getPercentageOfCompletedOnTime(userService.getCurrentUser().getId());
        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/number/{stage}")
    @Operation(summary = "Отримати кількість завдань авторизованого користувача за стадією")
    public ResponseEntity<Integer> getNumberByUserAndStage(@PathVariable(value = "stage") String stageName) {
        Integer number = taskService.getNumberByAssigneeAndStage(userService.getCurrentUser().getId(), stageName);
        return new ResponseEntity<>(number, HttpStatus.OK);
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
