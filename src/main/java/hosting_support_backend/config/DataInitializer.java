package hosting_support_backend.config;

import hosting_support_backend.entity.AIResponse;
import hosting_support_backend.entity.FAQ;
import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.entity.HostingPlan;
import hosting_support_backend.entity.Message;
import hosting_support_backend.entity.Notification;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.WorkflowLog;
import hosting_support_backend.entity.enums.HostingStatus;
import hosting_support_backend.entity.enums.Priority;
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.entity.enums.SenderType;
import hosting_support_backend.entity.enums.TicketStatus;
import hosting_support_backend.repository.AIResponseRepository;
import hosting_support_backend.repository.FAQRepository;
import hosting_support_backend.repository.HostingAccountRepository;
import hosting_support_backend.repository.HostingPlanRepository;
import hosting_support_backend.repository.MessageRepository;
import hosting_support_backend.repository.NotificationRepository;
import hosting_support_backend.repository.TicketRepository;
import hosting_support_backend.repository.UserRepository;
import hosting_support_backend.repository.WorkflowLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final HostingPlanRepository hostingPlanRepository;
  private final HostingAccountRepository hostingAccountRepository;
  private final TicketRepository ticketRepository;
  private final NotificationRepository notificationRepository;
  private final MessageRepository messageRepository;
  private final AIResponseRepository aiResponseRepository;
  private final FAQRepository faqRepository;
  private final WorkflowLogRepository workflowLogRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    System.out.println("🟢 DataInitializer starting...");

    String[] userNames = {"reda", "maria", "nina", "david", "julia", "omar", "liam", "sophia", "michael", "leah", "noah", "emma"};
    String[] fullNames = {"Reda", "Maria Santos", "Nina Patel", "David Brown", "Julia Gray", "Omar Khalid", "Liam Carter", "Sophia Turner", "Michael Hall", "Leah Adams", "Noah Kim", "Emma Lopez"};
    String[] emails = {"reda@example.com", "maria.santos@example.com", "nina.patel@example.com", "david.brown@example.com", "julia.gray@example.com", "omar.khalid@example.com", "liam.carter@example.com", "sophia.turner@example.com", "michael.hall@example.com", "leah.adams@example.com", "noah.kim@example.com", "emma.lopez@example.com"};
    String[] phones = {"+1-202-555-0173", "+1-202-555-0114", "+1-202-555-0135", "+1-202-555-0186", "+1-202-555-0127", "+1-202-555-0198", "+1-202-555-0149", "+1-202-555-0150", "+1-202-555-0151", "+1-202-555-0152", "+1-202-555-0153", "+1-202-555-0154"};
    boolean[] enabledFlags = {true, true, false, true, true, false, true, true, true, false, true, true};

    if (userRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        userRepository.save(User.builder()
                .userName(userNames[i])
                .fullName(fullNames[i])
                .email(emails[i])
                .phone(phones[i])
                .password(passwordEncoder.encode(i == 0 ? "12345678" : "Pass@" + (1000 + i)))
                .role(i == 0 ? Role.ADMIN : Role.USER)
                .enabled(enabledFlags[i])
                .build());
      }
    } else {
      userRepository.findByEmail("reda@example.com").ifPresentOrElse(
        admin -> {
          if (admin.getRole() != Role.ADMIN || admin.getEnabled() != Boolean.TRUE) {
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
          }
        },
        () -> {
          userRepository.save(User.builder()
                  .userName("reda")
                  .fullName("Reda")
                  .email("reda@example.com")
                  .phone("+1-202-555-0173")
                  .password(passwordEncoder.encode("12345678"))
                  .role(Role.ADMIN)
                  .enabled(true)
                  .build());
        }
      );
    }
    List<User> users = userRepository.findAll();
    System.out.println("Users count after seeding: " + users.size());

    String[] planNames = {"Starter", "Growth", "Business", "Pro", "Advanced", "Premium", "Enterprise", "Launch", "Scale", "Elite", "Ultimate", "Developer"};
    String[] planDescriptions = {"Good for small sites", "Best for growing blogs", "Business-grade hosting", "High-performance hosting", "Advanced developer features", "Premium support included", "Enterprise reliability", "Launch your first project", "Scale with confidence", "Elite performance", "Ultimate resources", "Developer sandbox"};
    Integer[] storageValues = {20, 50, 100, 150, 200, 300, 500, 80, 120, 250, 400, 60};
    Integer[] bandwidthValues = {200, 500, 1000, 1200, 1500, 2000, 2500, 300, 800, 1600, 2200, 400};
    Integer[] emailAccounts = {5, 10, 15, 20, 25, 30, 40, 8, 12, 18, 35, 6};
    Boolean[] sslIncluded = {true, true, false, true, true, true, true, false, true, true, true, false};
    Double[] realPrices = {199.0, 399.0, 699.0, 999.0, 1299.0, 1699.0, 2499.0, 299.0, 899.0, 1499.0, 1999.0, 499.0};

    if (hostingPlanRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        hostingPlanRepository.save(HostingPlan.builder()
                .name(planNames[i])
                .description(planDescriptions[i])
                .price(realPrices[i])
                .storage(storageValues[i])
                .bandwidth(bandwidthValues[i])
                .emailAccounts(emailAccounts[i])
                .sslIncluded(sslIncluded[i])
                .build());
      }
    } else {
      // Auto-update existing dummy prices (< 100.0) in MySQL table to realistic values
      List<HostingPlan> existingPlans = hostingPlanRepository.findAll();
      for (int i = 0; i < existingPlans.size(); i++) {
        HostingPlan p = existingPlans.get(i);
        if (p.getPrice() == null || p.getPrice() < 100.0) {
          p.setPrice(realPrices[i % realPrices.length]);
          hostingPlanRepository.save(p);
        }
      }
    }
    List<HostingPlan> plans = hostingPlanRepository.findAll();
    System.out.println("Hosting plans count after seeding: " + plans.size());

    String[] domains = {"redahost.com", "fastweb.io", "cloudgen.net", "sitepro.org", "hostingzone.com", "webedge.io", "serverhub.net", "hostify.org", "netlaunch.com", "webmatrix.io", "brighthost.net", "cloudnest.org"};
    HostingStatus[] hostStatuses = {HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.EXPIRED, HostingStatus.ACTIVE, HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.ACTIVE, HostingStatus.EXPIRED, HostingStatus.ACTIVE, HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.ACTIVE};

    if (hostingAccountRepository.count() == 0 && !users.isEmpty() && !plans.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        hostingAccountRepository.save(HostingAccount.builder()
                .domainName(domains[i])
                .status(hostStatuses[i])
                .startDate(LocalDate.now().minusDays(i * 5L))
                .expirationDate(LocalDate.now().plusDays(30 + i * 3L))
                .user(users.get(i % users.size()))
                .hostingPlan(plans.get(i % plans.size()))
                .build());
      }
    }
    List<HostingAccount> accounts = hostingAccountRepository.findAll();
    System.out.println("Hosting accounts count after seeding: " + accounts.size());

    TicketStatus[] ticketStatuses = {TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED, TicketStatus.CLOSED, TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.OPEN, TicketStatus.RESOLVED, TicketStatus.CLOSED, TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED};
    Priority[] ticketPriorities = {Priority.HIGH, Priority.MEDIUM, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW};
    String[] ticketSubjects = {"Impossible de se connecter", "Site web très lent", "Problème de messagerie e-mail", "Configuration du certificat SSL", "Erreur de connexion base de données", "Échec de paiement d'hébergement", "Délai de propagation DNS", "Échec du transfert de fichiers FTP", "Rapport de lien cassé", "Suspension de compte imprévue", "Restauration de sauvegarde", "Optimisation des performances"};
    String[] ticketDescriptions = {"Une erreur survient lors de la connexion à mon espace.", "Le site charge très lentement sur mobile.", "Les e-mails entrants ne sont plus distribués.", "J'ai besoin d'aide pour configurer le certificat SSL.", "Ma base de données MySQL refuse la connexion.", "La transaction de renouvellement n'a pas pu aboutir.", "Les modifications DNS ne sont pas encore visibles.", "Le transfert de fichiers via FTP échoue avec une erreur serveur.", "Un lien vers une page produit affiche une erreur 404.", "Mon compte d'hébergement a été suspendu de manière inattendue.", "Je souhaite restaurer la sauvegarde de la veille.", "Les pages web mettent trop de temps à se charger."};

    List<Ticket> existingTickets = ticketRepository.findAll();
    if (existingTickets.isEmpty() && !users.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        ticketRepository.save(Ticket.builder()
                .subject(ticketSubjects[i])
                .description(ticketDescriptions[i])
                .status(ticketStatuses[i])
                .priority(ticketPriorities[i])
                .user(users.get(i % users.size()))
                .build());
      }
    } else {
      for (int i = 0; i < Math.min(existingTickets.size(), ticketSubjects.length); i++) {
        Ticket t = existingTickets.get(i);
        t.setSubject(ticketSubjects[i]);
        t.setDescription(ticketDescriptions[i]);
        ticketRepository.save(t);
      }
    }
    List<Ticket> tickets = ticketRepository.findAll();
    System.out.println("Tickets count after seeding in French: " + tickets.size());

    String[] notificationTitles = {"Billing updated", "Account alert", "Migration complete", "New plan offer", "Security notice", "Renewal reminder", "Support reply", "Service notice", "Usage limit", "Feature launch", "System maintenance", "Policy update"};
    String[] notificationMessages = {"Your billing info has been updated.", "A security alert was detected on your account.", "Your migration finished successfully.", "A new hosting plan is available.", "Please review your security settings.", "Your renewal is due soon.", "Support has replied to your ticket.", "A service notice is available.", "You are near your usage limit.", "A new feature was launched.", "Scheduled maintenance is coming.", "Our policy has been updated."};

    if (notificationRepository.count() == 0 && !users.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        notificationRepository.save(Notification.builder()
                .title(notificationTitles[i])
                .message(notificationMessages[i])
                .read(i % 4 == 0)
                .user(users.get(i % users.size()))
                .hostingAccount(accounts.isEmpty() ? null : accounts.get(i % accounts.size()))
                .build());
      }
    }
    System.out.println("Notifications count after seeding: " + notificationRepository.count());

    String[] frMessageContents = {
      "Bonjour, pouvez-vous vérifier le statut de mon ticket ?",
      "Bonjour, notre équipe a bien reçu votre demande et analyse votre compte.",
      "J'ai déjà effectué le paiement de renouvellement de mon domaine.",
      "Votre paiement a été confirmé, votre service d'hébergement est réactivé.",
      "Mon site web est toujours hors ligne après la modification DNS.",
      "La propagation DNS est en cours et sera effective dans quelques minutes.",
      "J'ai besoin de plus d'espace de stockage sur mon hébergement.",
      "Votre formule a été mise à jour avec 50 Go d'espace supplémentaire.",
      "Merci de mettre à jour le certificat SSL pour mon domaine.",
      "Le certificat SSL Let's Encrypt a été régénéré et activé avec succès.",
      "Mes e-mails entrants me reviennent avec une erreur de livraison.",
      "Le filtre anti-spam a été ajusté et vos e-mails fonctionnent à nouveau."
    };
    SenderType[] frSenderTypes = {
      SenderType.USER, SenderType.ADMIN,
      SenderType.USER, SenderType.ADMIN,
      SenderType.USER, SenderType.ADMIN,
      SenderType.USER, SenderType.ADMIN,
      SenderType.USER, SenderType.ADMIN,
      SenderType.USER, SenderType.ADMIN
    };

    List<Message> existingMessages = messageRepository.findAll();
    if (existingMessages.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        messageRepository.save(Message.builder()
                .content(frMessageContents[i])
                .sender(frSenderTypes[i])
                .ticket(tickets.get((i / 2) % tickets.size()))
                .user(frSenderTypes[i] == SenderType.ADMIN ? users.get(0) : users.get((i / 2) % users.size()))
                .build());
      }
    } else {
      for (int i = 0; i < Math.min(existingMessages.size(), frMessageContents.length); i++) {
        Message msg = existingMessages.get(i);
        msg.setContent(frMessageContents[i]);
        msg.setSender(frSenderTypes[i]);
        if (frSenderTypes[i] == SenderType.ADMIN && !users.isEmpty()) {
          msg.setUser(users.get(0));
        }
        messageRepository.save(msg);
      }
    }
    System.out.println("Messages count after seeding: " + messageRepository.count());

    String[] faqQuestions = {
      "Comment changer d'offre d'hébergement ?",
      "Comment réinitialiser mon mot de passe ?",
      "Puis-je migrer mon nom de domaine ?",
      "Que comprend le certificat SSL inclus ?",
      "Comment restaurer une sauvegarde ?",
      "Comment créer un compte e-mail ?",
      "Comment vérifier ma consommation de bande passante ?",
      "Puis-je utiliser un nom de domaine personnalisé ?",
      "Comment ouvrir un ticket de support ?",
      "Puis-je connecter une base de données MySQL ?",
      "Comment activer le CDN de mon site ?",
      "Comment résilier mon offre d'hébergement ?"
    };
    String[] faqAnswers = {
      "Rendez-vous dans la rubrique 'Formules d'Hébergement' de votre espace client et choisissez 'Changer de formule'.",
      "Utilisez le lien 'Mot de passe oublié' présent sur la page de connexion.",
      "Oui, la migration de nom de domaine est entièrement disponible et prise en charge.",
      "Le certificat SSL sécurise l'ensemble du trafic web de votre domaine via un chiffrement HTTPS.",
      "Accédez à l'outil de restauration des sauvegardes disponible sur votre tableau de bord.",
      "Rendez-vous dans la section 'Messagerie' et cliquez sur 'Ajouter un compte e-mail'.",
      "Consultez vos métriques d'utilisation dans l'onglet 'Statistiques & Bande passante'.",
      "Oui, ajoutez simplement votre domaine personnalisé dans votre tableau de bord client.",
      "Cliquez sur l'onglet 'Support' puis sélectionnez 'Créer un ticket'.",
      "Utilisez l'outil 'Bases de données' pour créer et raccorder vos bases MySQL.",
      "Activez l'option CDN depuis l'onglet 'Performance & Optimisation'.",
      "Vous pouvez gérer ou résilier votre formule depuis l'espace 'Facturation & Formules'."
    };

    List<FAQ> existingFaqs = faqRepository.findAll();
    if (existingFaqs.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        faqRepository.save(FAQ.builder()
                .question(faqQuestions[i])
                .answer(faqAnswers[i])
                .category(i % 2 == 0 ? "Facturation" : "Configuration")
                .build());
      }
    } else {
      for (int i = 0; i < Math.min(existingFaqs.size(), faqQuestions.length); i++) {
        FAQ f = existingFaqs.get(i);
        f.setQuestion(faqQuestions[i]);
        f.setAnswer(faqAnswers[i]);
        f.setCategory(i % 2 == 0 ? "Facturation" : "Configuration");
        faqRepository.save(f);
      }
    }
    List<FAQ> faqs = faqRepository.findAll();
    System.out.println("FAQs count after seeding in French: " + faqs.size());

    String[] workflowNames = {"Deployment", "Backup", "Monitoring", "Scaling", "SSL renewal", "Database sync", "Cache clear", "Security scan", "Email sync", "DNS update", "Analytics import", "Resource cleanup"};
    if (workflowLogRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        workflowLogRepository.save(WorkflowLog.builder()
                .workflowName(workflowNames[i])
                .executionStatus(i % 3 == 0 ? "FAILED" : "SUCCESS")
                .executionDate(LocalDateTime.now().minusDays(i))
                .build());
      }
    }
    List<WorkflowLog> workflowLogs = workflowLogRepository.findAll();
    System.out.println("Workflow logs count after seeding: " + workflowLogs.size());

    if (aiResponseRepository.count() == 0 && !tickets.isEmpty() && !workflowLogs.isEmpty() && !faqs.isEmpty()) {
      for (int i = 0; i < 12; i++) {
        aiResponseRepository.save(AIResponse.builder()
                .prompt("Generate response for ticket: " + ticketSubjects[i % ticketSubjects.length])
                .response("AI answer for issue: " + ticketDescriptions[i % ticketDescriptions.length])
                .provider("OpenAI")
                .confidenceScore(0.75 + i * 0.02)
                .ticket(tickets.get(i % tickets.size()))
                .workflowLog(workflowLogs.get(i % workflowLogs.size()))
                .faq(faqs.get(i % faqs.size()))
                .build());
      }
    }
    System.out.println("AI responses count after seeding: " + aiResponseRepository.count());

    System.out.println("✅ Default data inserted cleanly.");
  }

}
