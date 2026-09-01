package hosting_support_backend.service;

import hosting_support_backend.dto.requests.WebsiteOrderDTO;
import hosting_support_backend.entity.WebsiteOrder;

import java.util.List;

public interface WebsiteOrderService {
    List<WebsiteOrder> getAll();
    WebsiteOrder getById(Long id);
    List<WebsiteOrder> getByUserId(Long userId);
    WebsiteOrder create(WebsiteOrderDTO dto);
    WebsiteOrder update(Long id, WebsiteOrderDTO dto);
    void delete(Long id);
}
