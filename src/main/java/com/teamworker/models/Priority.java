package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Priority {

    @Id
    @Column(name = "priority_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "column_name")
    private String name;

    @OneToMany(mappedBy = "priority", fetch = FetchType.LAZY)
    private List<Task> tasks;
}
