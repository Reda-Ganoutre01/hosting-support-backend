package hosting_support_backend.service;

import hosting_support_backend.dto.requests.WorkflowLogRequestDTO;
import hosting_support_backend.entity.WorkflowLog;
import hosting_support_backend.repository.WorkflowLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowLogServiceImpl implements WorkflowLogService {

    private final WorkflowLogRepository workflowLogRepository;

    @Override
    public WorkflowLog create(WorkflowLogRequestDTO dto) {
        WorkflowLog workflowLog = WorkflowLog.builder()
                .workflowName(dto.getWorkflowName())
                .executionStatus(dto.getExecutionStatus())
                .build();

        return workflowLogRepository.save(workflowLog);
    }

    @Override
    public WorkflowLog update(Long id, WorkflowLogRequestDTO dto) {
        WorkflowLog existing = workflowLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowLog not found with id: " + id));

        existing.setWorkflowName(dto.getWorkflowName());
        existing.setExecutionStatus(dto.getExecutionStatus());
    

        return workflowLogRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!workflowLogRepository.existsById(id)) {
            throw new RuntimeException("WorkflowLog not found with id: " + id);
        }
        workflowLogRepository.deleteById(id);
    }

    @Override
    public WorkflowLog getById(Long id) {
        return workflowLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowLog not found with id: " + id));
    }

    @Override
    public List<WorkflowLog> getAll() {
        return workflowLogRepository.findAll();
    }
}
