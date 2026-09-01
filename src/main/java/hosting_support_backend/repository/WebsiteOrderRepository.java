package hosting_support_backend.repository;

import hosting_support_backend.entity.WebsiteOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteOrderRepository extends JpaRepository<WebsiteOrder, Long> {
    List<WebsiteOrder> findByUserIdOrderByIdDesc(Long userId);
    List<WebsiteOrder> findByDomainNameContainingIgnoreCase(String domainName);
}
