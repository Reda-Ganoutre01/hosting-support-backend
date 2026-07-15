package hosting_support_backend.service;

import hosting_support_backend.dto.requests.HostingAccountRequestDTO;
import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.entity.HostingPlan;
import hosting_support_backend.entity.User;
import hosting_support_backend.repository.HostingAccountRepository;
import hosting_support_backend.repository.HostingPlanRepository;
import hosting_support_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HostingAccountServiceImpl implements HostingAccountService {

    private final HostingAccountRepository hostingAccountRepository;
    private final UserRepository userRepository;
    private final HostingPlanRepository hostingPlanRepository;

    @Override
    public HostingAccount create(HostingAccountRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        HostingPlan hostingPlan = hostingPlanRepository.findById(dto.getHostingPlanId())
                .orElseThrow(() -> new RuntimeException("HostingPlan not found with id: " + dto.getHostingPlanId()));

        HostingAccount hostingAccount = HostingAccount.builder()
                .domainName(dto.getDomainName())
                .status(dto.getStatus())
                .startDate(dto.getStartDate())
                .expirationDate(dto.getExpirationDate())
                .user(user)
                .hostingPlan(hostingPlan)
                .build();

        return hostingAccountRepository.save(hostingAccount);
    }

    @Override
    public HostingAccount update(Long id, HostingAccountRequestDTO dto) {
        HostingAccount existing = hostingAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HostingAccount not found with id: " + id));

        existing.setDomainName(dto.getDomainName());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setExpirationDate(dto.getExpirationDate());

        return hostingAccountRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!hostingAccountRepository.existsById(id)) {
            throw new RuntimeException("HostingAccount not found with id: " + id);
        }
        hostingAccountRepository.deleteById(id);
    }

    @Override
    public HostingAccount getById(Long id) {
        return hostingAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HostingAccount not found with id: " + id));
    }

    @Override
    public List<HostingAccount> getAll() {
        return hostingAccountRepository.findAll();
    }

    @Override
    public List<HostingAccount> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return hostingAccountRepository.findAll().stream()
                .filter(ha -> ha.getUser().getId().equals(userId))
                .toList();
    }
}
