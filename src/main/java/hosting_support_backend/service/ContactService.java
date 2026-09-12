package hosting_support_backend.service;

import hosting_support_backend.dto.requests.ContactRequestDTO;
import hosting_support_backend.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact create(ContactRequestDTO dto);
    Contact getById(Long id);
    List<Contact> getAll();
    void delete(Long id);
}