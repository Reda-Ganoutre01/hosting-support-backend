package hosting_support_backend.service;

import hosting_support_backend.dto.requests.HostingAccountRequestDTO;
import hosting_support_backend.entity.HostingAccount;

import java.util.List;

public interface HostingAccountService {
    HostingAccount create(HostingAccountRequestDTO dto);
    HostingAccount update(Long id, HostingAccountRequestDTO dto);
    void delete(Long id);
    HostingAccount getById(Long id);
    List<HostingAccount> getAll();
    List<HostingAccount> getByUserId(Long userId);
}
