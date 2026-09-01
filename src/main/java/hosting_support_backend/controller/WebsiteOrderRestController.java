package hosting_support_backend.controller;

import hosting_support_backend.dto.requests.WebsiteOrderDTO;
import hosting_support_backend.entity.WebsiteOrder;
import hosting_support_backend.service.WebsiteOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/websiteOrders")
@RequiredArgsConstructor
public class WebsiteOrderRestController {

    private final WebsiteOrderService websiteOrderService;

    @GetMapping
    public ResponseEntity<List<WebsiteOrderDTO>> getAll() {
        List<WebsiteOrderDTO> dtos = websiteOrderService.getAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebsiteOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(websiteOrderService.getById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WebsiteOrderDTO>> getByUserId(@PathVariable Long userId) {
        List<WebsiteOrderDTO> dtos = websiteOrderService.getByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<WebsiteOrderDTO> create(@RequestBody WebsiteOrderDTO dto) {
        WebsiteOrder created = websiteOrderService.create(dto);
        return ResponseEntity.ok(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebsiteOrderDTO> update(@PathVariable Long id, @RequestBody WebsiteOrderDTO dto) {
        WebsiteOrder updated = websiteOrderService.update(id, dto);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        websiteOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private WebsiteOrderDTO toDTO(WebsiteOrder order) {
        return WebsiteOrderDTO.builder()
                .id(order.getId())
                .domainName(order.getDomainName())
                .siteType(order.getSiteType())
                .price(order.getPrice())
                .period(order.getPeriod())
                .status(order.getStatus())
                .productCount(order.getProductCount())
                .productVolume(order.getProductVolume())
                .productRanges(order.getProductRanges())
                .productSpecs(order.getProductSpecs())
                .hasVideoCompany(order.getHasVideoCompany())
                .hasVideoPromo(order.getHasVideoPromo())
                .hasVideo360(order.getHasVideo360())
                .orderDate(order.getOrderDate())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userName(order.getUser() != null ? (order.getUser().getFullName() != null ? order.getUser().getFullName() : order.getUser().getUserName()) : null)
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .build();
    }
}
