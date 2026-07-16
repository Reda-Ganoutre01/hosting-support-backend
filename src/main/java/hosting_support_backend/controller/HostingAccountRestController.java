package hosting_support_backend.controller;


import hosting_support_backend.dto.requests.HostingAccountRequestDTO;
import hosting_support_backend.dto.response.HostingAccountResponseDTO;
import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.service.HostingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hostingAccounts")
@RequiredArgsConstructor
public class HostingAccountRestController {

    private final HostingAccountService hostingAccountService;

    @GetMapping
    public ResponseEntity<List<HostingAccountResponseDTO>> getAll(){
        List<HostingAccountResponseDTO> dtos = hostingAccountService.getAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HostingAccountResponseDTO> getById(@PathVariable long id){
        HostingAccount hostingAccount = hostingAccountService.getById(id);
        return ResponseEntity.ok(toResponseDTO(hostingAccount));
    }

    @PostMapping
    public ResponseEntity<HostingAccountResponseDTO> create(@RequestBody HostingAccountRequestDTO hostingAccount){
        HostingAccount createdHostingAccount = hostingAccountService.create(hostingAccount);
        return ResponseEntity.ok(toResponseDTO(createdHostingAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HostingAccountResponseDTO>  update(@PathVariable long id,@RequestBody HostingAccountRequestDTO hostingAccount){
        HostingAccount updatedHostingAccount = hostingAccountService.update(id, hostingAccount);
        return ResponseEntity.ok(toResponseDTO(updatedHostingAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delete(@PathVariable long id){
        hostingAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private HostingAccountResponseDTO toResponseDTO(HostingAccount hostingAccount) {
        return HostingAccountResponseDTO.builder()
                .id(hostingAccount.getId())
                .domainName(hostingAccount.getDomainName())
                .status(hostingAccount.getStatus())
                .startDate(hostingAccount.getStartDate())
                .expirationDate(hostingAccount.getExpirationDate())
                .userId(hostingAccount.getUser().getId())
                .hostingPlanId(hostingAccount.getHostingPlan().getId())
                .build();
    }
}
