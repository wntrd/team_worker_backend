package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ProjectStage {

    @Id
    @Column(name = "project_stage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_stage_name")
    private String name;

    @OneToMany(mappedBy = "projectStage", fetch = FetchType.LAZY)
    private List<Project> projects;
}
