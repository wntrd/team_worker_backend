package com.teamworker.rest.manager;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.UserDto;
import com.teamworker.dtos.UserStatsDto;
import com.teamworker.models.User;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/manager/users")
@Tag(name = "/api/v1/manager/users", description = "Контролер для керування користувачами")
public class UserManagerRestController {

    private final UserService userService;

    @Autowired
    public UserManagerRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "get/all")
    @Operation(summary = "Отримати всіх користувачів")
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.getAll();

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDto> result = users.stream().map(UserDto::fromUser).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "get/all/manager")
    @Operation(summary = "Отримати всіх користувачів з проектів авторизованого менеджера")
    public ResponseEntity<List<UserDto>> getAllByCurrentManager() {
        List<User> users = userService.getAllByManager(userService.getCurrentUser().getId());

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDto> result = users.stream().map(UserDto::fromUser).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "get/stats/users/all")
    @Operation(summary = "Отримати всіх користувачів з проектів авторизованого менеджера зі статистикою")
    public ResponseEntity<List<UserStatsDto>> getAllWithStatsByManager() {
        Map<User, List<Integer>> usersWithStats = userService.getAllWithStatsByManager(userService.getCurrentUser().getId());

        if (usersWithStats == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<UserStatsDto> result = new ArrayList<>();
        usersWithStats.entrySet().forEach(entry -> result.add(UserStatsDto.fromUserWithStats(entry.getKey(), entry.getValue())));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "get/{id}")
    @Operation(summary = "Отримати користувача за id")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/add/position/{id}")
    @Operation(summary = "Додати користувачу посаду")
    public ResponseEntity<UserDto> addPosition(
            @PathVariable(value = "id") Long id,
            @RequestBody PositionDto positionDto) throws ParseException {

        User user = userService.addPosition(id, positionDto.toPosition());

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/delete/position/{id}")
    @Operation(summary = "Видалити користувачу посаду")
    public ResponseEntity<UserDto> deletePosition(
            @PathVariable(value = "id") Long id,
            @RequestBody PositionDto positionDto) throws ParseException {

        User user = userService.deletePosition(id, positionDto.toPosition());

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
