package hosting_support_backend.service;

import hosting_support_backend.dto.requests.FAQRequestDTO;
import hosting_support_backend.entity.FAQ;

import java.util.List;

public interface FAQService {
    FAQ create(FAQRequestDTO dto);
    FAQ update(Long id, FAQRequestDTO dto);
    void delete(Long id);
    FAQ getById(Long id);
    List<FAQ> getAll();
}
