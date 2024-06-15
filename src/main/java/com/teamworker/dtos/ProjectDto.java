package com.teamworker.dtos;

import com.teamworker.models.Position;
import com.teamworker.models.enums.ProjectStage;
import com.teamworker.models.enums.ProjectType;
import com.teamworker.models.Project;
import lombok.Data;

import java.text.ParseException;
import java.util.List;

@Data
public class ProjectDto {

    private Long id;
    private String name;
    private String createTime;
    private ProjectStage projectStage;
    private ProjectType projectType;
    private List<Position> positions;

    public Project toProject() {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setCreateTime(createTime);
        project.setProjectStage(projectStage);
        project.setProjectType(projectType);
        project.setPositions(positions);
        return project;
    }

    public static ProjectDto fromProject(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setCreateTime(project.getCreateTime());
        projectDto.setProjectStage(project.getProjectStage());
        projectDto.setProjectType(project.getProjectType());
        projectDto.setPositions(project.getPositions());
        return projectDto;
    }
}
