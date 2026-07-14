package hosting_support_backend.service;

import hosting_support_backend.dto.requests.WorkflowLogRequestDTO;
import hosting_support_backend.entity.WorkflowLog;

import java.util.List;

public interface WorkflowLogService {
    WorkflowLog create(WorkflowLogRequestDTO dto);
    WorkflowLog update(Long id, WorkflowLogRequestDTO dto);
    void delete(Long id);
    WorkflowLog getById(Long id);
    List<WorkflowLog> getAll();
}
