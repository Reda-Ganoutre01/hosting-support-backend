package hosting_support_backend.controller;


import hosting_support_backend.dto.requests.HostingPlanRequestDTO;
import hosting_support_backend.dto.response.HostingPlanResponseDTO;
import hosting_support_backend.entity.HostingPlan;
import hosting_support_backend.service.HostingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hostingPlans")
@RequiredArgsConstructor
public class HostingPlanRestController {

      private final HostingPlanService hostingPlanService;

    @GetMapping
    public ResponseEntity<List<HostingPlanResponseDTO>> getAll(){
        List<HostingPlanResponseDTO> dtos = hostingPlanService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HostingPlanResponseDTO> getById(@PathVariable long id){
        HostingPlan hostingPlan = hostingPlanService.getById(id);
        return ResponseEntity.ok(toResponseDTO(hostingPlan));
    }

    @PostMapping
    public ResponseEntity<HostingPlanResponseDTO> create(@RequestBody
                                                             HostingPlanRequestDTO hosingPlan){
        HostingPlan createdHostingPaln = hostingPlanService.create(hosingPlan);
        return ResponseEntity.ok(toResponseDTO(createdHostingPaln));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HostingPlanResponseDTO>  update(@PathVariable long id,@RequestBody HostingPlanRequestDTO hostingAccount){
        HostingPlan updatedHostingAccount = hostingPlanService.update(id, hostingAccount);
        return ResponseEntity.ok(toResponseDTO(updatedHostingAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delete(@PathVariable long id){
        hostingPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private HostingPlanResponseDTO toResponseDTO(HostingPlan hostingPlan) {
        return HostingPlanResponseDTO.builder()
                .id(hostingPlan.getId())
                .name(hostingPlan.getName())
                .description(hostingPlan.getDescription())
                .price(hostingPlan.getPrice())
                .storage(hostingPlan.getStorage())
                .bandwidth(hostingPlan.getBandwidth())
                .emailAccounts(hostingPlan.getEmailAccounts())
                .sslIncluded(hostingPlan.getSslIncluded())
                .build();
    }
}
