package hosting_support_backend.entity;

import jakarta.persistence.Entity;

@Entity
public class FAQ {
  Long id;
  String question;
  String answer;
  String category;
}
