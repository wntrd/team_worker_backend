package com.teamworker.rest.admin;

import com.teamworker.dtos.UserDto;
import com.teamworker.models.User;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin/users")
@Tag(name = "/api/v1/admin/users", description = "Контролер для керування користувачами")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "get/{id}")
    @Operation(summary = "Отримати користувача за id")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // а чого не в auth контроллері?
    @PostMapping(value = "register")
    @Operation(summary = "Зареєструвати користувача")
    public ResponseEntity registerUser(@RequestBody UserDto userDto) {
        User user = userService.register(userDto.toUser());

        if (user == null) {

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
