package com.teamworker.rest.manager;

import com.teamworker.dtos.PositionDto;
import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.services.PositionService;
import com.teamworker.services.ProjectService;
import com.teamworker.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "/api/v1/manager/position")
@Tag(name = "/api/v1/manager/position", description = "Контролер для керування посадами (manager)")
public class PositionManagerRestController {

    private final PositionService positionService;
    private final ProjectService projectService;

    public PositionManagerRestController(PositionService positionService, ProjectService projectService) {
        this.positionService = positionService;
        this.projectService = projectService;
    }

    @PostMapping(value = "add")
    @Operation(summary = "Додати посаду")
    public ResponseEntity<PositionDto> addPosition(@RequestBody PositionDto positionDto) throws ParseException {
        Project project = projectService.getById(positionDto.getProject().getId());
        if(project == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        positionService.add(positionDto.toPosition());

        return new ResponseEntity<>(positionDto, HttpStatus.CREATED);
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
