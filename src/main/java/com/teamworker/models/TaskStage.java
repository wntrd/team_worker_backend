package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class TaskStage {

    @Id
    @Column(name = "task_stage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_stage_name")
    private String name;

    @OneToMany(mappedBy = "stage", fetch = FetchType.LAZY)
    private List<Task> tasks;
}
