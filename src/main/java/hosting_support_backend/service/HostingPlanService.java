package hosting_support_backend.service;

import hosting_support_backend.dto.requests.HostingPlanRequestDTO;
import hosting_support_backend.entity.HostingPlan;

import java.util.List;

public interface HostingPlanService {
    HostingPlan create(HostingPlanRequestDTO dto);
    HostingPlan update(Long id, HostingPlanRequestDTO dto);
    void delete(Long id);
    HostingPlan getById(Long id);
    List<HostingPlan> getAll();
}
