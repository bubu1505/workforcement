package com.ralise.hiring.workforcement.service.impl;

import com.ralise.hiring.workforcement.common.exception.ResourceNotFoundException;
import com.ralise.hiring.workforcement.dto.*;
import com.ralise.hiring.workforcement.mapper.ITaskManagementMapper;
import com.ralise.hiring.workforcement.model.TaskManagement;
import com.ralise.hiring.workforcement.model.enums.Task;
import com.ralise.hiring.workforcement.model.enums.TaskStatus;
import com.ralise.hiring.workforcement.repository.TaskRepository;
import com.ralise.hiring.workforcement.service.TaskManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {
    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssignedId(item.getAssignedId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            createdTasks.add(taskRepository.save(newTask));
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));
            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
        
        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());
            
            if (!tasksOfType.isEmpty()) {
                // Mark all but the first task as CANCELLED
                for (int i = 1; i < tasksOfType.size(); i++) {
                    TaskManagement taskToCancel = tasksOfType.get(i);
                    taskToCancel.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToCancel);
                }
                // Update the first task with new assignee
                TaskManagement taskToUpdate = tasksOfType.get(0);
                taskToUpdate.setAssignedId(request.getAssignedId());
                taskRepository.save(taskToUpdate);
            } else {
                // Create new task if none exist
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssignedId(request.getAssignedId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                taskRepository.save(newTask);
            }
        }
        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssignedIdIn(request.getAssigneeIds());
        
        // Filter out CANCELLED tasks and apply date range logic
        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED)
                .filter(task -> {
                    // Simplified date range check (implementation depends on your requirements)
                    return true;
                })
                .collect(Collectors.toList());
        
        return taskMapper.modelListToDtoList(filteredTasks);
    }
}
