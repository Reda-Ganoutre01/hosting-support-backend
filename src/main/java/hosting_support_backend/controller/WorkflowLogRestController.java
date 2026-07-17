package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.WorkflowLogRequestDTO;
import hosting_support_backend.dto.response.WorkflowLogResponseDTO;
import hosting_support_backend.entity.WorkflowLog;
import hosting_support_backend.service.WorkflowLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workflowLogs")
@RequiredArgsConstructor
public class WorkflowLogRestController {

    private final WorkflowLogService workflowLogService;

    @GetMapping
    public ResponseEntity<List<WorkflowLogResponseDTO>> getAll() {
        List<WorkflowLogResponseDTO> dtos = workflowLogService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowLogResponseDTO> getById(@PathVariable long id) {
        WorkflowLog workflowLog = workflowLogService.getById(id);
        return ResponseEntity.ok(toResponseDTO(workflowLog));
    }

    @PostMapping
    public ResponseEntity<WorkflowLogResponseDTO> create(@RequestBody WorkflowLogRequestDTO dto) {
        WorkflowLog workflowLog = workflowLogService.create(dto);
        return ResponseEntity.ok(toResponseDTO(workflowLog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkflowLogResponseDTO> update(@PathVariable long id, @RequestBody WorkflowLogRequestDTO dto) {
        WorkflowLog workflowLog = workflowLogService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(workflowLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        workflowLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private WorkflowLogResponseDTO toResponseDTO(WorkflowLog workflowLog) {
        return WorkflowLogResponseDTO.builder()
                .id(workflowLog.getId())
                .workflowName(workflowLog.getWorkflowName())
                .executionStatus(workflowLog.getExecutionStatus())
                .executionDate(workflowLog.getExecutionDate())
                .build();
    }
}
