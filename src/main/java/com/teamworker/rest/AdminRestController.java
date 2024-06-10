package com.teamworker.rest;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.UserDto;
import com.teamworker.exceptions.UserNotFoundException;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/admin")
@Tag(name = "/api/v1/admin", description = "Контролер для адміністрування")
public class AdminRestController {

    private final UserService userService;
    private final PositionService positionService;

    @Autowired
    public AdminRestController(UserService userService, PositionService positionService) {
        this.positionService = positionService;
        this.userService = userService;
    }

    @GetMapping(value = "users/get/{id}")
    @Operation(summary = "Отримати користувача за id")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "positions/get/all")
    @Operation(summary = "Отримати всі посади")
    public ResponseEntity<List<PositionDto>> getPositions() {
        List<Position> positions = positionService.getAll();

        if(positions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<PositionDto> result = positions.stream().map(PositionDto::fromPosition).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "positions/add")
    @Operation(summary = "Додати посаду")
    public ResponseEntity<PositionDto> addPosition(@RequestBody PositionDto positionDto) {

        positionService.add(positionDto.toPosition());

        return new ResponseEntity<>(positionDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "positions/update/{id}")
    @Operation(summary = "Оновити посаду")
    public ResponseEntity<PositionDto> updatePosition(
            @PathVariable(name = "id") Long id,
            @RequestBody PositionDto positionDto) {

        if(positionDto.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Position position = positionService.update(id, positionDto.toPosition());

        if(position == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        positionDto = PositionDto.fromPosition(position);

        return new ResponseEntity<>(positionDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "positions/delete/{id}")
    @Operation(summary = "Видалити посаду")
    public ResponseEntity<PositionDto> deletePosition(@PathVariable(name = "id") Long id) {

        Position position = positionService.getById(id);
        if(position == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<User> users = userService.findUsersWithPosition(position);
        if(users != null) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        positionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
