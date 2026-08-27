package hosting_support_backend.service;

import hosting_support_backend.dto.requests.AIResponseRequestDTO;
import hosting_support_backend.entity.AIResponse;
import hosting_support_backend.entity.FAQ;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.repository.AIResponseRepository;
import hosting_support_backend.repository.FAQRepository;
import hosting_support_backend.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIResponseServiceImpl implements AIResponseService {

    private final AIResponseRepository aiResponseRepository;
    private final TicketRepository ticketRepository;
    private final FAQRepository faqRepository;

    @Override
    public AIResponse create(AIResponseRequestDTO dto) {
        Ticket ticket = null;
        if (dto.getTicketId() != null && dto.getTicketId() > 0) {
            ticket = ticketRepository.findById(dto.getTicketId()).orElse(null);
        }

        String prompt = dto.getPrompt() != null ? dto.getPrompt().trim() : "";
        
        // Search FAQ table in MySQL backend for matching question & answer
        FAQ matchingFaq = findMatchingFaq(prompt);
        String responseText;
        double confidence;

        if (matchingFaq != null) {
            responseText = matchingFaq.getAnswer();
            confidence = 0.98;
        } else {
            responseText = generateFallbackKnowledgeResponse(prompt);
            confidence = calculateConfidenceScore(prompt);
        }

        AIResponse aiResponse = AIResponse.builder()
                .prompt(prompt)
                .response(responseText)
                .provider("Vala AI Engine & FAQ Base")
                .confidenceScore(confidence)
                .ticket(ticket)
                .faq(matchingFaq)
                .build();

        return aiResponseRepository.save(aiResponse);
    }

    private FAQ findMatchingFaq(String prompt) {
        if (prompt.isBlank()) return null;

        List<FAQ> allFaqs = faqRepository.findAll();
        if (allFaqs.isEmpty()) return null;

        String lowerPrompt = prompt.toLowerCase();

        // 1. Direct contains check on question or prompt
        for (FAQ faq : allFaqs) {
            if (faq.getQuestion() != null && lowerPrompt.contains(faq.getQuestion().toLowerCase())) {
                return faq;
            }
            if (faq.getQuestion() != null && faq.getQuestion().toLowerCase().contains(lowerPrompt)) {
                return faq;
            }
        }

        // 2. Keyword matching on FAQ questions & answers
        String[] words = lowerPrompt.split("\\s+");
        FAQ bestMatch = null;
        int maxHits = 0;

        for (FAQ faq : allFaqs) {
            int hits = 0;
            String faqText = (faq.getQuestion() + " " + faq.getAnswer() + " " + faq.getCategory()).toLowerCase();
            for (String word : words) {
                if (word.length() > 2 && faqText.contains(word)) {
                    hits++;
                }
            }
            if (hits > maxHits) {
                maxHits = hits;
                bestMatch = faq;
            }
        }

        return maxHits > 0 ? bestMatch : null;
    }

    private String generateFallbackKnowledgeResponse(String prompt) {
        String lower = prompt.toLowerCase();
        if (lower.contains("domain") || lower.contains("domaine") || lower.contains("dns")) {
            return "Yes, add the domain in your dashboard. Set your DNS nameservers to ns1.valahosting.com and ns2.valahosting.com.";
        }
        if (lower.contains("wordpress") || lower.contains("wp")) {
            return "To install WordPress: Log into your Vala cPanel, go to 'Softaculous Apps Installer', select WordPress and click 'Install Now'.";
        }
        if (lower.contains("cpanel")) {
            return "Access your cPanel via your client dashboard under 'My Hosting Accounts' or directly at https://your-domain.com:2083.";
        }
        if (lower.contains("password") || lower.contains("mot de passe")) {
            return "Use the reset link on the login page to reset your password.";
        }
        return "Thank you for your inquiry. You can browse our FAQ section or open a support ticket if your question requires human assistance.";
    }

    private double calculateConfidenceScore(String prompt) {
        String lower = prompt.toLowerCase();
        if (lower.contains("domain") || lower.contains("cpanel") || lower.contains("wordpress") || lower.contains("password") || lower.contains("upgrade") || lower.contains("ssl")) {
            return 0.95;
        }
        return 0.65;
    }

    @Override
    public AIResponse update(Long id, AIResponseRequestDTO dto) {
        AIResponse existing = aiResponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AIResponse not found with id: " + id));

        existing.setPrompt(dto.getPrompt());
        existing.setResponse(dto.getResponse());
        existing.setProvider(dto.getProvider());
        existing.setConfidenceScore(dto.getConfidenceScore());

        return aiResponseRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!aiResponseRepository.existsById(id)) {
            throw new RuntimeException("AIResponse not found with id: " + id);
        }
        aiResponseRepository.deleteById(id);
    }

    @Override
    public AIResponse getById(Long id) {
        return aiResponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AIResponse not found with id: " + id));
    }

    @Override
    public List<AIResponse> getAll() {
        return aiResponseRepository.findAll();
    }

    @Override
    public Optional<AIResponse> getByTicketId(Long ticketId) {
        return aiResponseRepository.findAll().stream()
                .filter(ar -> ar.getTicket() != null && ar.getTicket().getId().equals(ticketId))
                .findFirst();
    }
}
