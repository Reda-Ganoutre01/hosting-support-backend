package hosting_support_backend.repository;

import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail (String email);

    @Query("SELECT u FROM User u WHERE " +
           "(:search IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:role IS NULL OR u.role = :role) " +
           "AND (:enabled IS NULL OR u.enabled = :enabled)")
    Page<User> findByFilters(@Param("search") String search,
                            @Param("role") Role role,
                            @Param("enabled") Boolean enabled,
                            Pageable pageable);
}
