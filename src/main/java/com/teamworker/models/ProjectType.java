package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class ProjectType {

    @Id
    @Column(name = "project_type_id")
    private Long id;

    @Column(name = "project_type_name")
    private String name;

    @OneToMany(mappedBy = "projectType", fetch = FetchType.LAZY)
    private Set<Project> projects;
}
