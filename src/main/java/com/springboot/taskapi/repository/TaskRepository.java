package com.springboot.taskapi.repository;

import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import com.springboot.taskapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // findBy -- Derived Query Methods
        // Id - findById

    List<Task> findByPriority(Priority priority); // select t from Task t where t.priority=?1
    List<Task> findByStatus(Status status); // select t from Task t where t.status=?1
    List<Task> findByPriorityAndStatus(Priority priority, Status status);
    // select t from Task t where t.priority=?1 and t.status=?2

    @Query("""
            select new com.springboot.taskapi.dto.response.TaskRespDto(
            t.id,
            t.title,
            t.description,
            t.dueDate,
            t.priority,
            t.status,
            t.createdAt,
            t.updatedAt
            )
            from Task t
            where (?1 IS NULL OR t.priority = ?1)
            AND  (?2 IS NULL OR t.status = ?2)
            AND (?3 IS NULL OR t.dueDate >= ?3)
            AND (?4 IS NULL OR t.dueDate <= ?4)
            """)
    List<TaskRespDto> dynamicFilterTask(Priority priority,
                                        Status status,
                                        LocalDate fromDate,
                                        LocalDate toDate);
}
