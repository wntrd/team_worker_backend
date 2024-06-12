package com.teamworker.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/projects")
@Tag(name = "/api/v1/admin/projects", description = "Контролер для керування проектами")
public class ProjectRestController {
}
