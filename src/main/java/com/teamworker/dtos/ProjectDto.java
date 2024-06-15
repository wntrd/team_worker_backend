package com.teamworker.dtos;

import com.teamworker.models.Position;
import com.teamworker.models.enums.ProjectStage;
import com.teamworker.models.enums.ProjectType;
import com.teamworker.models.Project;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class ProjectDto {

    private Long id;
    private String name;
    private String createTime;
    private ProjectStage projectStage;
    private ProjectType projectType;
    private List<Position> positions;

    public Project toProject() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Date parseDate = simpleDateFormat.parse(createTime);
        Timestamp parsedCreateTime = new java.sql.Timestamp(parseDate.getTime());

        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setCreateTime(parsedCreateTime);
        project.setProjectStage(projectStage);
        project.setProjectType(projectType);
        project.setPositions(positions);
        return project;
    }

    public static ProjectDto fromProject(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setCreateTime(new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(project.getCreateTime()));
        projectDto.setProjectStage(project.getProjectStage());
        projectDto.setProjectType(project.getProjectType());
        projectDto.setPositions(project.getPositions());
        return projectDto;
    }
}
