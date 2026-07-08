package hosting_support_backend.repository;


import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.entity.enums.HostingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostingAccountRepository extends JpaRepository<HostingAccount,Long> {
    List<HostingAccount> findByUserId(Long userId);
    List<HostingAccount> findByStatus(HostingStatus status);
    Optional<HostingAccount> findByDomainName(String domainName);
    boolean existsByDomainName(String domainName);
}
