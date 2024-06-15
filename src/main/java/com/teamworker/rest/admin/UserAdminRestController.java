package com.teamworker.rest.admin;

import com.teamworker.dtos.MainUserInfoDto;
import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.UserDto;
import com.teamworker.models.User;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/admin/users")
@Tag(name = "/api/v1/admin/users", description = "Контролер адміністрування користувачів")
public class UserAdminRestController {

    private final UserService userService;

    @Autowired
    public UserAdminRestController(UserService userService) {
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

    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Оновити користувача")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody UserDto userDto) {

        User user = userService.update(id, userDto.toUser());

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/add/position/{id}")
    @Operation(summary = "Оновити користувача")
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

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Видалити користувача")
    public ResponseEntity<UserDto> deleteUser(@PathVariable(value = "id") Long id) {

        if(userService.getById(id) == null || !userService.getById(id).getAssignedTasks().isEmpty()
        || !userService.getById(id).getCreatedTasks().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
