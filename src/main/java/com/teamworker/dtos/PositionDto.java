package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Position;
import lombok.Data;

import java.text.ParseException;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDto {

    private Long id;
    private String name;
    private ProjectDto projectDto;

    public Position toPosition() throws ParseException {
        Position position = new Position();
        position.setId(id);
        position.setName(name);
        position.setProject(projectDto.toProject());
        return position;
    }

    public static PositionDto fromPosition(Position position) {
        PositionDto positionDto = new PositionDto();
        positionDto.setId(position.getId());
        positionDto.setName(position.getName());
        positionDto.setProjectDto(ProjectDto.fromProject(position.getProject()));
        return positionDto;
    }
}
