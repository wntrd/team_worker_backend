package com.teamworker.services.impl;

import com.teamworker.models.*;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.models.enums.TaskType;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.TaskRepository;
import com.teamworker.repositories.UserRepository;
import com.teamworker.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.teamworker.models.enums.TaskStage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
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

    public List<Task> getAllByManager(User manager) {
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
    public List<Task> getAllByStage(Long id, String stageName) {
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.valueOf(stageName));
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
    public List<Task> getAllByStageForManager(User manager, String stageName) {
        List<Task> tasks = taskRepository.getAllByStageAndProject_Manager(TaskStage.valueOf(stageName), manager);
        for (Task task : tasks) {
            if (task.getDueTime().before(new Timestamp(new Date().getTime()))) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStageForManager - {} tasks added", tasks.size());
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
        else if (Objects.equals(stageName, RELEASED.name())) {
            if (task.getStartTime() == null) {
                task.setStartTime(new Timestamp(new Date().getTime()));
            }
            else if (task.getEndTime() == null) {
                task.setEndTime(new Timestamp(new Date().getTime()));
            }
        }

        task.setStage(TaskStage.valueOf(stageName));
        taskRepository.save(task);
        log.info("IN changeStage - task with id {} updated", task.getId());
        return task;
    }

    @Override
    public Task update(Long id, Task task) {
        Task foundTask = taskRepository.findById(id).orElse(null);
        if (foundTask == null) {
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
    public Integer getNumberByAssigneeAndType(Long id, String typeName) {
        return taskRepository.countTasksByAssigneeIdAndType(id, TaskType.valueOf(typeName));
    }

    @Override
    public Map<String, Integer> getNumbersWithTypesByAssignee(Long id) {
        Map<String, Integer> numbersWithTypes = new LinkedHashMap<>();

        for (TaskType type : TaskType.values()) {
            Integer number = this.getNumberByAssigneeAndType(id, type.name());
            numbersWithTypes.put(type.name(), number);
        }
        return numbersWithTypes;
    }

    @Override
    public Map<String, Integer> getNumbersWithStagesByAssignee(Long id) {
        Map<String, Integer> numbersWithStages = new LinkedHashMap<>();

        String[] stages = {"Створено", "В процесі", "На перевірці", "Виконано"};

        for (TaskStage stage : TaskStage.values()) {
            Integer number = this.getNumberByAssigneeAndStage(id, stage.name());
            numbersWithStages.put(stages[stage.ordinal()], number);
        }
        return numbersWithStages;
    }

    @Override
    public List<Task> getAllByAssigneeAndCreateTime(Long id, String time1, String time2) throws ParseException {
        Timestamp parsedTime1 = new Timestamp(dateFormat.parse(time1).getTime());
        Timestamp parsedTime2 = new Timestamp(dateFormat.parse(time2).getTime());
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndCreateTimeBetween(id, parsedTime1, parsedTime2);
        log.info("IN getAllByAssigneeAndCreateTime - {} tasks found", tasks.size());
        return tasks;
    }

    @Override
    public Task getTaskWithClosestDueTimeByAssignee(Long id) {
        List<Task> tasks = new ArrayList<>();

        tasks.addAll(taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.CREATED));
        tasks.addAll(taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.IN_PROGRESS));

        if (tasks.isEmpty()) {
            return null;
        }

        Task result = tasks.stream().min(Comparator.comparing(Task::getDueTime)).orElse(null);

        return result;
    }

    @Override
    public String getAverageTimeOfCompletingByAssignee(Long id) {
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.RELEASED);

        if (tasks.isEmpty()) {
            return "00:00:00";
        }

        List<Long> timesInSeconds = new ArrayList<>();
        tasks.stream().forEach(task -> timesInSeconds.add(TimeUnit.MILLISECONDS.toSeconds(
                task.getEndTime().getTime() - task.getStartTime().getTime())));

        Long totalTimeInSeconds = timesInSeconds.stream().reduce(0L, Long::sum);
        Long result = Math.round((double) totalTimeInSeconds / tasks.size());

        Integer hours = Math.toIntExact(result / 3600);
        Integer minutes = Math.toIntExact(result % 3600 / 60);
        Integer seconds = Math.toIntExact(result % 3600 % 60 );


        String resultString = (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes+ ":"
                + (seconds < 10 ? "0" : "") + seconds;
        return resultString;
    }

    @Override
    public Integer getNumberOfMostProductiveMonthByAssignee(Long id) {
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.RELEASED);

        if (tasks.isEmpty()) {
            return 0;
        }

        Integer result = 0;

        LocalDateTime oldestTaskEndTime = tasks.stream().min(Comparator.comparing(Task::getEndTime))
                .orElseThrow(NoSuchElementException::new).getEndTime().toLocalDateTime();

        LocalDateTime newestTaskEndTime = tasks.stream().max(Comparator.comparing(Task::getEndTime))
                .orElseThrow(NoSuchElementException::new).getEndTime().toLocalDateTime();

        while(!(oldestTaskEndTime.isAfter(newestTaskEndTime))) {
            LocalDateTime finalOldestTaskEndTime = oldestTaskEndTime;
            List<Task> filteredTasks = tasks.stream().filter(task ->
                    (task.getEndTime().toLocalDateTime().getMonth() == finalOldestTaskEndTime.getMonth()) &&
                            (task.getEndTime().toLocalDateTime().getYear() == finalOldestTaskEndTime.getYear())
            ).collect(Collectors.toList());

            if (result < filteredTasks.size()) {
                result = filteredTasks.size();
            }
            oldestTaskEndTime = oldestTaskEndTime.plusMonths(1);
        }

        return result;
    }

    @Override
    public Map<String, Integer> getNumbersWithMonthsByAssignee(Long id) {
        List<Task> tasks = taskRepository.getAllByAssigneeIdAndStage(id, TaskStage.RELEASED);

        String[] months = {"Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень",
                "Жовтень", "Листопад", "Грудень"};

        LocalDateTime currentTime = LocalDateTime.now();
        log.info("test {}", currentTime);

        Map<String, Integer> numbersWithMonths = new LinkedHashMap<>();

        while (currentTime.getMonth() != currentTime.minusMonths(7).getMonth() &&
                currentTime.getYear() != currentTime.minusMonths(7).getYear()) {
            LocalDateTime finalCurrentTime = currentTime;
            List<Task> filteredTasks = tasks.stream().filter(task ->
                    (task.getEndTime().toLocalDateTime().getMonth() == finalCurrentTime.getMonth()) &&
                            (task.getEndTime().toLocalDateTime().getYear() == finalCurrentTime.getYear()))
                    .collect(Collectors.toList());

            numbersWithMonths.put(months[currentTime.getMonth().getValue() - 1], filteredTasks.size());
            currentTime = currentTime.minusMonths(1);
        }

        return numbersWithMonths;
    }
}
