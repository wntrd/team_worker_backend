package com.teamworker.rest;

import com.teamworker.dtos.UserDto;
import com.teamworker.models.Position;
import com.teamworker.models.User;
import com.teamworker.services.PositionService;
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
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/users")
@Tag(name = "/api/v1/users", description = "Контролер для керування користувачами")
public class UserRestController {

    private final UserService userService;
    private final PositionService positionService;

    @Autowired
    public UserRestController(UserService userService, PositionService positionService) {
        this.userService = userService;
        this.positionService = positionService;
    }

    @GetMapping(value = "get/{username}")
    @Operation(summary = "Отримати користувача за логіном")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "get/all/position/{id}")
    @Operation(summary = "Отримати користувачів за посадою")
    public ResponseEntity<List<UserDto>> getUserByPosition(@PathVariable(name = "id") Long positionId) {
        Position position = positionService.getById(positionId);
        List<User> users = userService.findUsersWithPosition(position);

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<UserDto> result = users.stream().map(UserDto::fromUser).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
