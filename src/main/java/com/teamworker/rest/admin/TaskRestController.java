package com.teamworker.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/tasks")
@Tag(name = "/api/v1/admin/tasks", description = "Контролер для керування завданнями")
public class TaskRestController {
}
