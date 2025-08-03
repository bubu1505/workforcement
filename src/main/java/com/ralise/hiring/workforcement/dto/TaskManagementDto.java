package com.ralise.hiring.workforcement.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ralise.hiring.workforcement.common.model.enums.ReferenceType;
import com.ralise.hiring.workforcement.model.enums.*;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
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
