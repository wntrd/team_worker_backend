package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class TaskType {

    @Id
    @Column(name = "task_type_id")
    private Long id;

    @Column(name = "task_type_name")
    private String name;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<Task> tasks;
}
