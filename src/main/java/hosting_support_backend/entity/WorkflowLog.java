package hosting_support_backend.entity;


import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class WorkflowLog {
    long id;
    String workflowName;
    String executionStatus;
    LocalDateTime executionDate;
}
