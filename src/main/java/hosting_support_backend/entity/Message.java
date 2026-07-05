package hosting_support_backend.entity;


import hosting_support_backend.entity.enums.SenderType;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Message {
    Long id;
    String content;
    SenderType senderType;
    LocalDateTime sendAt;

}
