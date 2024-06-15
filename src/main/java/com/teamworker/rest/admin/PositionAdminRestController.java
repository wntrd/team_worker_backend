package com.teamworker.rest.admin;

import com.teamworker.dtos.PositionDto;
import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.services.PositionService;
import com.teamworker.services.ProjectService;
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
@RequestMapping(value = "/api/v1/admin/positions")
@Tag(name = "/api/v1/admin/positions", description = "Контролер для керування посадами")
public class PositionAdminRestController {

    private final PositionService positionService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public PositionAdminRestController(PositionService positionService, UserService userService, ProjectService projectService) {
        this.positionService = positionService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping(value = "get/all")
    @Operation(summary = "Отримати всі посади")
    public ResponseEntity<List<PositionDto>> getPositions() {
        List<Position> positions = positionService.getAll();

        if(positions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<PositionDto> result = positions.stream().map(PositionDto::fromPosition).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "get/{id}")
    @Operation(summary = "Отримати посаду за ідентифікатором")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable(name = "id") Long id) {
        Position position = positionService.getById(id);
        if(position == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        PositionDto result = PositionDto.fromPosition(position);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "add")
    @Operation(summary = "Додати посаду")
    public ResponseEntity<PositionDto> addPosition(@RequestBody PositionDto positionDto) throws ParseException {
        Project project = projectService.getById(positionDto.getProjectDto().getId());
        if(project == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        positionService.add(positionDto.toPosition());

        return new ResponseEntity<>(positionDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "update/{id}")
    @Operation(summary = "Оновити посаду")
    public ResponseEntity<PositionDto> updatePosition(
            @PathVariable(name = "id") Long id,
            @RequestBody PositionDto positionDto) throws ParseException {

        Position position = positionService.update(id, positionDto.toPosition());

        if(position == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        positionDto = PositionDto.fromPosition(position);

        return new ResponseEntity<>(positionDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "delete/{id}")
    @Operation(summary = "Видалити посаду")
    public ResponseEntity<PositionDto> deletePosition(@PathVariable(name = "id") Long id) {

        Position position = positionService.getById(id);
        if(position == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



        positionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
