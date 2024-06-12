package com.teamworker.rest.admin;

import com.teamworker.dtos.MainUserInfoDto;
import com.teamworker.dtos.UserDto;
import com.teamworker.models.User;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/admin/users")
@Tag(name = "/api/v1/admin/users", description = "Контролер для керування користувачами")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "get/all")
    @Operation(summary = "Отримати всіх користувачів")
    public ResponseEntity<List<UserDto>> getAll(@PathVariable(name = "id") Long id) {
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
    public ResponseEntity<MainUserInfoDto> updateUser(
            @PathVariable(value = "id") Long id,
            @RequestBody MainUserInfoDto userDto) {

        User user = userService.update(id, userDto.toUser());

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        MainUserInfoDto result = MainUserInfoDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
