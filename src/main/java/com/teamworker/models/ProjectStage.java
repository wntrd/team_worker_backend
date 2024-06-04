package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class ProjectStage {

    @Id
    @Column(name = "project_stage_id")
    private Long id;

    @Column(name = "project_stage_name")
    private String name;

    @OneToMany(mappedBy = "projectStage", fetch = FetchType.LAZY)
    private Set<Project> projects;
}
