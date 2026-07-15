package hosting_support_backend.service;

import hosting_support_backend.dto.requests.FAQRequestDTO;
import hosting_support_backend.entity.FAQ;
import hosting_support_backend.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;

    @Override
    public FAQ create(FAQRequestDTO dto) {
        FAQ faq = FAQ.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .category(dto.getCategory())
                .build();

        return faqRepository.save(faq);
    }

    @Override
    public FAQ update(Long id, FAQRequestDTO dto) {
        FAQ existing = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));

        existing.setQuestion(dto.getQuestion());
        existing.setAnswer(dto.getAnswer());
        existing.setCategory(dto.getCategory());

        return faqRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new RuntimeException("FAQ not found with id: " + id);
        }
        faqRepository.deleteById(id);
    }

    @Override
    public FAQ getById(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
    }

    @Override
    public List<FAQ> getAll() {
        return faqRepository.findAll();
    }
}
