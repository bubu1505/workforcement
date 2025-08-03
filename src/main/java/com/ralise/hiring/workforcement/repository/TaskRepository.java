package com.ralise.hiring.workforcement.repository;

import com.ralise.hiring.workforcement.common.model.enums.ReferenceType;
import com.ralise.hiring.workforcement.model.TaskManagement;
import com.ralise.hiring.workforcement.model.enums.TaskStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository{
    Optional<TaskManagement> findById(Long id);
    TaskManagement save(TaskManagement task);
    List<TaskManagement> findAll();
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
    List<TaskManagement> findByAssignedIdIn(List<Long> assigneeIds);
    //List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
}
