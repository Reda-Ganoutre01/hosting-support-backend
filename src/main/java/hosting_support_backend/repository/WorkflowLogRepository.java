package hosting_support_backend.repository;

import hosting_support_backend.entity.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WorkflowLogRepository extends JpaRepository<WorkflowLog,Long> {
    List<WorkflowLog> findByExecutionStatus(String executionStatus);
    List <WorkflowLog> findByWorkFlowName (String workflowName);
}
