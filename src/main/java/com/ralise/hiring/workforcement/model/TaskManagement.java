package com.ralise.hiring.workforcement.model;

import com.ralise.hiring.workforcement.common.model.enums.ReferenceType;
import com.ralise.hiring.workforcement.model.enums.*;
import lombok.Data;

@Data
public class TaskManagement {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assignedId;
    private Long taskDeadlineTime;
    private Priority priority;
}