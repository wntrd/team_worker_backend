package com.teamworker.rest.admin;

import com.teamworker.dtos.PositionDto;
import com.teamworker.dtos.ProjectDto;
import com.teamworker.models.Project;
import com.teamworker.services.ProjectService;
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
@RequestMapping(value = "/api/v1/admin/projects")
@Tag(name = "/api/v1/admin/projects", description = "Контролер для керування проектами")
public class ProjectAdminRestController {

    private final ProjectService projectService;

    @Autowired
    ProjectAdminRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/get/all")
    @Operation(summary = "Отримати всі проекти")
    public ResponseEntity<List<ProjectDto>> getAll() {
        List<Project> projects = projectService.getAll();
        if(projects == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<ProjectDto> result = projects.stream().map(ProjectDto::fromProject).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    @Operation(summary = "Отримати проект за ідентифікатором")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable(value = "id") Long id) {
        Project project = projectService.getById(id);
        if(project == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ProjectDto result = ProjectDto.fromProject(project);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Додати проект")
    public ResponseEntity<ProjectDto> addProject(@RequestBody ProjectDto projectDto) throws ParseException {
        Project project = projectService.add(projectDto.toProject());
        if(project == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ProjectDto result = ProjectDto.fromProject(project);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Оновити проект")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable(value = "id") Long id,
            @RequestBody ProjectDto projectDto) throws ParseException {

        Project project = projectService.update(id, projectDto.toProject());

        if(project == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ProjectDto result = ProjectDto.fromProject(project);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Видалити проект")
    public ResponseEntity<PositionDto> deleteProject(@PathVariable(value = "id") Long id) {
        if(projectService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
