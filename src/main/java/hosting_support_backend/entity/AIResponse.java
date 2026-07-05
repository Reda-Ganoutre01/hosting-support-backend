package hosting_support_backend.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class  AIResponse {
  long id;
  String prompt;
  String response;
  String provider;
  Double confidenceScore;
  LocalDateTime generatedAt;


}
