package hosting_support_backend.service;

import hosting_support_backend.dto.requests.WebsiteOrderDTO;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.WebsiteOrder;
import hosting_support_backend.exception.ResourceNotFoundException;
import hosting_support_backend.repository.UserRepository;
import hosting_support_backend.repository.WebsiteOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WebsiteOrderServiceImpl implements WebsiteOrderService {

    private final WebsiteOrderRepository websiteOrderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WebsiteOrder> getAll() {
        return websiteOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public WebsiteOrder getById(Long id) {
        return websiteOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande de site introuvable avec l'ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WebsiteOrder> getByUserId(Long userId) {
        return websiteOrderRepository.findByUserIdOrderByIdDesc(userId);
    }

    @Override
    public WebsiteOrder create(WebsiteOrderDTO dto) {
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }

        WebsiteOrder order = WebsiteOrder.builder()
                .domainName(dto.getDomainName() != null ? dto.getDomainName() : "domaine.ma")
                .siteType(dto.getSiteType() != null ? dto.getSiteType() : "Site E-commerce PrestaShop")
                .price(dto.getPrice() != null ? dto.getPrice() : 9999.0)
                .period(dto.getPeriod() != null ? dto.getPeriod() : "One Time")
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .productCount(dto.getProductCount())
                .productVolume(dto.getProductVolume())
                .productRanges(dto.getProductRanges())
                .productSpecs(dto.getProductSpecs())
                .hasVideoCompany(dto.getHasVideoCompany())
                .hasVideoPromo(dto.getHasVideoPromo())
                .hasVideo360(dto.getHasVideo360())
                .orderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now())
                .user(user)
                .build();

        return websiteOrderRepository.save(order);
    }

    @Override
    public WebsiteOrder update(Long id, WebsiteOrderDTO dto) {
        WebsiteOrder existing = getById(id);
        if (dto.getDomainName() != null) existing.setDomainName(dto.getDomainName());
        if (dto.getSiteType() != null) existing.setSiteType(dto.getSiteType());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getPeriod() != null) existing.setPeriod(dto.getPeriod());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getProductCount() != null) existing.setProductCount(dto.getProductCount());
        if (dto.getProductVolume() != null) existing.setProductVolume(dto.getProductVolume());
        if (dto.getProductRanges() != null) existing.setProductRanges(dto.getProductRanges());
        if (dto.getProductSpecs() != null) existing.setProductSpecs(dto.getProductSpecs());
        if (dto.getHasVideoCompany() != null) existing.setHasVideoCompany(dto.getHasVideoCompany());
        if (dto.getHasVideoPromo() != null) existing.setHasVideoPromo(dto.getHasVideoPromo());
        if (dto.getHasVideo360() != null) existing.setHasVideo360(dto.getHasVideo360());

        return websiteOrderRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        WebsiteOrder order = getById(id);
        websiteOrderRepository.delete(order);
    }
}
