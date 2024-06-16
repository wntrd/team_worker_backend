package com.teamworker.services.impl;

import com.teamworker.models.*;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.TaskRepository;
import com.teamworker.repositories.UserRepository;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.teamworker.models.enums.TaskStage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss");


    @Override
    public Task add(Task task) {

        if (task.getDueTime().before(task.getCreateTime())) {
            return null;
        }

        task.setOverdue(false);

        log.info("IN add - {} task added", task.getName());
        return taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            log.warn("IN getById - no task found by id: {}", id);
            return null;
        }

        log.info("IN getById - {} task added", task.getId());
        return task;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime())) &&
                    (task.getStage() == CREATED || task.getStage() == IN_PROGRESS)) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAll - {} tasks added", tasks.size());
        return tasks;
    }

    public List<Task> getAllByManager(Long id) {
        User manager = userService.getById(id);
        List<Task> tasks = new ArrayList<>();

        manager.getManagerProjects().stream().forEach(project ->
                tasks.addAll(this.getAllByProject(project)));

        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime())) &&
                    (task.getStage() == CREATED || task.getStage() == IN_PROGRESS)) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAll - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByStage(String stageName) {
        List<Task> tasks = taskRepository.getAllByAssigneeAndStage(userService.getCurrentUser(),
                TaskStage.valueOf(stageName));
        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime())) &&
                    (task.getStage() == CREATED || task.getStage() == IN_PROGRESS)) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStage - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByStageForAdmin(String stageName) {
        List<Task> tasks = taskRepository.getAllByStage(TaskStage.valueOf(stageName));
        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime()))) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStageForAdmin - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByStageForManager(String stageName, Long id) {
        User manager = userService.getById(id);
        List<Task> tasks = taskRepository.getAllByStageAndProject_Manager(TaskStage.valueOf(stageName), manager);
        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime()))) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStageForAdmin - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public Task changeStage(Long taskId, String stageName) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return null;
        }

        if (Objects.equals(stageName, TaskStage.IN_PROGRESS.name())) {
            task.setStartTime(new Timestamp(new Date().getTime()));
            task.setEndTime(null);
        }
        else if (Objects.equals(stageName, ON_REVIEW.name())) {
            if (task.getStartTime() == null) {
                task.setStartTime(new Timestamp(new Date().getTime()));
            }
            task.setEndTime(new Timestamp(new Date().getTime()));
        }

        task.setStage(TaskStage.valueOf(stageName));
        taskRepository.save(task);
        log.info("IN changeStage - task with id {} updated", task.getId());
        return task;
    }

    @Override
    public Task update(Long id, Task task) {
        Task foundTask = taskRepository.findById(id).orElse(null);
        if(foundTask == null) {
            return null;
        }

        if (task.getDueTime().before(task.getCreateTime())) {
            return null;
        }

        foundTask.setName(task.getName());
        foundTask.setDescription(task.getDescription());
        foundTask.setDueTime(task.getDueTime());
        foundTask.setEndTime(task.getEndTime());
        foundTask.setAssignee(task.getAssignee());
        foundTask.setProject(task.getProject());
        foundTask.setPriority(task.getPriority());
        foundTask.setType(task.getType());
        foundTask.setLastEditTime(task.getLastEditTime());
        foundTask.setOverdue(false);

        log.info("IN update - {} task updated", task.getId());

        return taskRepository.save(foundTask);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            log.warn("IN delete - no task found by id: {}", id);
            return;
        }

        taskRepository.deleteById(id);
        log.info("IN delete - task with id: {} successfully deleted", id);
    }

    @Override
    public List<Task> getAllByProject(Project project) {
        List<Task> tasks = taskRepository.getAllByProject(project);
        log.info("IN getAllByProject - {} tasks found", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByAssignee(Long id) {
        List<Task> tasks = taskRepository.getAllByAssigneeId(id);
        log.info("IN getAllByAssignee - {} tasks found", tasks.size());
        return tasks;
    }

    @Override
    public Integer getPercentageOfCompletedOnTime(Long id) {
        List<Task> tasks = this.getAllByAssignee(id);
        Integer numberOfCompletedTasks = this.getNumberByAssigneeAndStage(id, RELEASED.name());
        List<Task> tasksOnTime = tasks.stream().filter(
                task -> (task.getEndTime() != null && task.getEndTime().before(task.getDueTime())
                        && Objects.equals(task.getStage(), RELEASED)))
                .collect(Collectors.toList());
        return Math.toIntExact(Math.round((double) tasksOnTime.size() / numberOfCompletedTasks * 100));
    }

    @Override
    public Integer getNumberByAssigneeAndStage(Long id, String stageName) {
        return taskRepository.countTasksByAssigneeIdAndStage(id, TaskStage.valueOf(stageName));
    }

    @Override
    public List<Task> getAllByAssigneeAndCreateTime(Long id, String time1, String time2) throws ParseException {
        Timestamp parsedTime1 = new Timestamp(dateFormat.parse(time1).getTime());
        Timestamp parsedTime2 = new Timestamp(dateFormat.parse(time2).getTime());
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndCreateTimeBetween(id, parsedTime1, parsedTime2);
        log.info("IN getAllByAssigneeAndCreateTime - {} tasks found", tasks.size());
        return tasks;
    }
}
