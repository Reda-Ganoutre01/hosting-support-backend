package hosting_support_backend.repository;


import hosting_support_backend.entity.HostingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostingPlanRepository extends JpaRepository<HostingPlan ,Long>{
    Optional<HostingPlan> findByName (String name);
    boolean existsByName(String name);
}
