package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Position;
import com.teamworker.models.Project;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDto {

    private Long id;
    private String name;
    private Project project;

    public Position toPosition() {
        Position position = new Position();
        position.setId(id);
        position.setName(name);
        position.setProject(project);
        return position;
    }

    public static PositionDto fromPosition(Position position) {
        PositionDto positionDto = new PositionDto();
        positionDto.setId(position.getId());
        positionDto.setName(position.getName());
        positionDto.setProject(position.getProject());
        return positionDto;
    }
}
