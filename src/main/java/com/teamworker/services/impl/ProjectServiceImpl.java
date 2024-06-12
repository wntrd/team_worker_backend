package com.teamworker.services.impl;

import com.teamworker.models.Project;
import com.teamworker.repositories.ProjectRepository;
import com.teamworker.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project add(Project project) {
        log.info("IN add - {} project added", project.getName());
        return projectRepository.save(project);
    }

    @Override
    public Project update(Long id, Project project) {
        Project foundProject = projectRepository.findById(id).orElse(null);
        if(foundProject == null) {
            return null;
        }
        foundProject.setName(project.getName());
        foundProject.setCreateTime(project.getCreateTime());
        foundProject.setProjectType(project.getProjectType());
        foundProject.setProjectStage(project.getProjectStage());

        log.info("IN update - {} project updated", project.getId());

        return projectRepository.save(foundProject);
    }

    @Override
    public void delete(Long id) {
       log.info("IN delete - {} project deleted", id);
       projectRepository.deleteById(id);
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = projectRepository.findAll();
        if(projects.isEmpty()) {
            return null;
        }
        log.info("IN getAll - {} projects found", projects.size());
        return projects;
    }

    @Override
    public Project getById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null) {
            return null;
        }
        log.info("IN getById {} project found", project.getName());
        return project;
    }
}
