package hosting_support_backend.repository;

import hosting_support_backend.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FAQRepository extends JpaRepository <FAQ , Long> {
    List<FAQ> findByCategory(String category);
    List<FAQ> findByQuestionContainingIgnoreCase(String question);

}
