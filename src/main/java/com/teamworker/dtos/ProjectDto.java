package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.enums.ProjectStage;
import com.teamworker.models.enums.ProjectType;
import com.teamworker.models.Project;
import lombok.Data;

import java.sql.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDto {

    private Long id;
    private String name;
    private Date createTime;
    private ProjectStage projectStage;
    private ProjectType projectType;

    public Project toProject() {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setCreateTime(createTime);
        project.setProjectStage(projectStage);
        project.setProjectType(projectType);
        return project;
    }

    public static ProjectDto fromProject(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setCreateTime(project.getCreateTime());
        projectDto.setProjectStage(project.getProjectStage());
        projectDto.setProjectType(project.getProjectType());
        return projectDto;
    }
}
