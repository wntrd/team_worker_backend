package com.teamworker.rest;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.ProjectDto;
import com.teamworker.dtos.TaskDto;
import com.teamworker.models.Project;
import com.teamworker.models.Task;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TaskDto result = TaskDto.fromTask(task);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "/get/all/{stage}")
    @Operation(summary = "Отримати всі завдання за стадією")
    public ResponseEntity<List<TaskDto>> getAllByStage(@PathVariable(value = "stage") String stageName) throws ParseException {
        List<Task> tasks = taskService.getAllByStage(userService.getCurrentUser().getId(), stageName);
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

        if (task == null) {
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

    @GetMapping(value = "/get/stats/average/time")
    @Operation(summary = "Отримати середню тривалість виконання завдання авторизованого користувача")
    public ResponseEntity<String> getAverageTimeOfCompletingByAssignee() throws JSONException {
        String time = taskService.getAverageTimeOfCompletingByAssignee(userService.getCurrentUser().getId());
        JSONObject response = new JSONObject();
        response.put("response", time);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/best/month")
    @Operation(summary = "Отримати найбільшу кількість виконаних завдання за місяць авторизованого користувача")
    public ResponseEntity<Integer> getNumberOfMostProductiveMonthByAssignee() {
        Integer number = taskService.getNumberOfMostProductiveMonthByAssignee(userService.getCurrentUser().getId());
        return new ResponseEntity<>(number, HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/months")
    @Operation(summary = "Отримати кількість виконаних завдання за місяцями авторизованого користувача")
    public ResponseEntity<String> getNumbersWithMonthsByAssignee() {
        Map<String, Integer> numbersWithMonths = taskService.getNumbersWithMonthsByAssignee(userService.getCurrentUser().getId());
        JSONArray array = new JSONArray();
        numbersWithMonths.entrySet().forEach(entry -> {
            JSONObject month = new JSONObject();
            try {
                month.put("name", entry.getKey());
                month.put("number", entry.getValue());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            array.put(month);
        });
        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/types")
    @Operation(summary = "Отримати кількість завдань за типами авторизованого користувача")
    public ResponseEntity<String> getNumbersWithTypesByAssignee() {
        Map<String, Integer> numbersWithTypes = taskService.getNumbersWithTypesByAssignee(userService.getCurrentUser().getId());
        JSONArray array = new JSONArray();
        numbersWithTypes.entrySet().forEach(entry -> {
            JSONObject type = new JSONObject();
            try {
                type.put("name", entry.getKey());
                type.put("number", entry.getValue());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            array.put(type);
        });
        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/stages")
    @Operation(summary = "Отримати кількість завдань за стадіями авторизованого користувача")
    public ResponseEntity<String> getNumbersWithStagesByAssignee() {
        Map<String, Integer> numbersWithStages = taskService.getNumbersWithStagesByAssignee(userService.getCurrentUser().getId());
        JSONArray array = new JSONArray();
        numbersWithStages.entrySet().forEach(entry -> {
            JSONObject stage = new JSONObject();
            try {
                stage.put("name", entry.getKey());
                stage.put("number", entry.getValue());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            array.put(stage);
        });
        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/stats/closest")
    @Operation(summary = "Отримати завдання з найкоротшим терміном виконання авторизованого користувача")
    public ResponseEntity<TaskDto> getTaskWithClosestDueTimeByAssignee()
            throws ParseException {
        Task task = taskService.getTaskWithClosestDueTimeByAssignee(userService.getCurrentUser().getId());

        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(TaskDto.fromTask(task), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Видалити завдання")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable(value = "id") Long id) {
        if (taskService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
