package hosting_support_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name ="workflow_logs")

public class WorkflowLog {
    @Id
            @GeneratedValue( strategy = GenerationType.IDENTITY)
    long id;

    @Column (nullable = false)
    String workflowName;

    @Column (nullable = false)
    String executionStatus;

    @Column(nullable = false)
    LocalDateTime executionDate;
}
