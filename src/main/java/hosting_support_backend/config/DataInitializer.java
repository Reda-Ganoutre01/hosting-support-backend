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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
  // private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    System.out.println("🟢 DataInitializer starting...");

    String[] userNames = {"alex", "maria", "nina", "david", "julia", "omar", "liam", "sophia", "michael", "leah", "noah", "emma"};
    String[] fullNames = {"Alex Johnson", "Maria Santos", "Nina Patel", "David Brown", "Julia Gray", "Omar Khalid", "Liam Carter", "Sophia Turner", "Michael Hall", "Leah Adams", "Noah Kim", "Emma Lopez"};
    String[] emails = {"alex.johnson@example.com", "maria.santos@example.com", "nina.patel@example.com", "david.brown@example.com", "julia.gray@example.com", "omar.khalid@example.com", "liam.carter@example.com", "sophia.turner@example.com", "michael.hall@example.com", "leah.adams@example.com", "noah.kim@example.com", "emma.lopez@example.com"};
    String[] phones = {"+1-202-555-0173", "+1-202-555-0114", "+1-202-555-0135", "+1-202-555-0186", "+1-202-555-0127", "+1-202-555-0198", "+1-202-555-0149", "+1-202-555-0150", "+1-202-555-0151", "+1-202-555-0152", "+1-202-555-0153", "+1-202-555-0154"};
    boolean[] enabledFlags = {true, true, false, true, true, false, true, true, true, false, true, true};

    List<User> users = new ArrayList<>();
    if (userRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        users.add(User.builder()
                .userName(userNames[i])
                .fullName(fullNames[i])
                .email(emails[i])
                .phone(phones[i])
                .password("Pass@" + (1000 + i))
                .role(Role.USER)
                .enabled(enabledFlags[i])
                .build());
      }
      userRepository.saveAll(users);
    } else {
      users = userRepository.findAll();
    }
    System.out.println("Users count after seeding: " + userRepository.count());

    String[] planNames = {"Starter", "Growth", "Business", "Pro", "Advanced", "Premium", "Enterprise", "Launch", "Scale", "Elite", "Ultimate", "Developer"};
    String[] planDescriptions = {"Good for small sites", "Best for growing blogs", "Business-grade hosting", "High-performance hosting", "Advanced developer features", "Premium support included", "Enterprise reliability", "Launch your first project", "Scale with confidence", "Elite performance", "Ultimate resources", "Developer sandbox"};
    Integer[] storageValues = {20, 50, 100, 150, 200, 300, 500, 80, 120, 250, 400, 60};
    Integer[] bandwidthValues = {200, 500, 1000, 1200, 1500, 2000, 2500, 300, 800, 1600, 2200, 400};
    Integer[] emailAccounts = {5, 10, 15, 20, 25, 30, 40, 8, 12, 18, 35, 6};
    Boolean[] sslIncluded = {true, true, false, true, true, true, true, false, true, true, true, false};

    List<HostingPlan> plans = new ArrayList<>();
    if (hostingPlanRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        plans.add(HostingPlan.builder()
                .name(planNames[i])
                .description(planDescriptions[i])
                .price(4.99 + i * 2)
                .storage(storageValues[i])
                .bandwidth(bandwidthValues[i])
                .emailAccounts(emailAccounts[i])
                .sslIncluded(sslIncluded[i])
                .build());
      }
      hostingPlanRepository.saveAll(plans);
    } else {
      plans = hostingPlanRepository.findAll();
    }
    System.out.println("Hosting plans count after seeding: " + hostingPlanRepository.count());

    String[] domains = {"redahost.com", "fastweb.io", "cloudgen.net", "sitepro.org", "hostingzone.com", "webedge.io", "serverhub.net", "hostify.org", "netlaunch.com", "webmatrix.io", "brighthost.net", "cloudnest.org"};
    HostingStatus[] hostStatuses = {HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.EXPIRED, HostingStatus.ACTIVE, HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.ACTIVE, HostingStatus.EXPIRED, HostingStatus.ACTIVE, HostingStatus.ACTIVE, HostingStatus.SUSPENDED, HostingStatus.ACTIVE};

    List<HostingAccount> accounts = new ArrayList<>();
    if (hostingAccountRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        accounts.add(HostingAccount.builder()
                .domainName(domains[i])
                .status(hostStatuses[i])
                .startDate(LocalDate.now().minusDays(i * 5L))
                .expirationDate(LocalDate.now().plusDays(30 + i * 3L))
                .user(users.get(i))
                .hostingPlan(plans.get(i))
                .build());
      }
      hostingAccountRepository.saveAll(accounts);
    } else {
      accounts = hostingAccountRepository.findAll();
    }
    System.out.println("Hosting accounts count after seeding: " + hostingAccountRepository.count());

    TicketStatus[] ticketStatuses = {TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED, TicketStatus.CLOSED, TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.OPEN, TicketStatus.RESOLVED, TicketStatus.CLOSED, TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED};
    Priority[] ticketPriorities = {Priority.HIGH, Priority.MEDIUM, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW};
    String[] ticketSubjects = {"Unable to login", "Website slow", "Email not working", "SSL setup", "Database issue", "Payment gateway error", "DNS propagation delay", "File upload failed", "Broken link report", "Account suspension", "Backup restore", "Performance tuning"};
    String[] ticketDescriptions = {"I see an error when signing in.", "The site is very slow on mobile.", "Incoming emails are not delivered.", "I need help configuring SSL.", "My database won't connect.", "Checkout does not complete.", "DNS changes have not propagated.", "Uploads fail with a server error.", "A product page link is broken.", "My account was suspended unexpectedly.", "I need to restore yesterday's backup.", "Pages are loading too slowly."};

    List<Ticket> tickets = new ArrayList<>();
    if (ticketRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        tickets.add(Ticket.builder()
                .subject(ticketSubjects[i])
                .description(ticketDescriptions[i])
                .status(ticketStatuses[i])
                .priority(ticketPriorities[i])
                .user(users.get(i))
                .build());
      }
      ticketRepository.saveAll(tickets);
    } else {
      tickets = ticketRepository.findAll();
    }
    System.out.println("Tickets count after seeding: " + ticketRepository.count());

    String[] notificationTitles = {"Billing updated", "Account alert", "Migration complete", "New plan offer", "Security notice", "Renewal reminder", "Support reply", "Service notice", "Usage limit", "Feature launch", "System maintenance", "Policy update"};
    String[] notificationMessages = {"Your billing info has been updated.", "A security alert was detected on your account.", "Your migration finished successfully.", "A new hosting plan is available.", "Please review your security settings.", "Your renewal is due soon.", "Support has replied to your ticket.", "A service notice is available.", "You are near your usage limit.", "A new feature was launched.", "Scheduled maintenance is coming.", "Our policy has been updated."};

    List<Notification> notifications = new ArrayList<>();
    if (notificationRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        notifications.add(Notification.builder()
                .title(notificationTitles[i])
                .message(notificationMessages[i])
                .read(i % 4 == 0)
                .user(users.get(i))
                .hostingAccount(accounts.get(i))
                .build());
      }
      notificationRepository.saveAll(notifications);
    }
    System.out.println("Notifications count after seeding: " + notificationRepository.count());

    String[] messageContents = {"Can you check the ticket status?", "I already sent the payment.", "The site is still offline.", "I need additional storage.", "Please update the SSL certificate.", "My emails are bouncing back.", "I found a security issue.", "Can you increase my bandwidth?", "The upload still fails.", "I need help restoring the site.", "Is the backup complete?", "The support chat says resolved."};
    SenderType[] senderTypes = {SenderType.USER, SenderType.ADMIN, SenderType.USER, SenderType.ADMIN, SenderType.USER, SenderType.ADMIN, SenderType.USER, SenderType.ADMIN, SenderType.USER, SenderType.ADMIN, SenderType.USER, SenderType.ADMIN};

    List<Message> messages = new ArrayList<>();
    if (messageRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        messages.add(Message.builder()
                .content(messageContents[i])
                .sender(senderTypes[i])
                .ticket(tickets.get(i))
                .user(users.get(i))
                .build());
      }
      messageRepository.saveAll(messages);
    }
    System.out.println("Messages count after seeding: " + messageRepository.count());

    String[] faqQuestions = {"How can I upgrade my plan?", "How do I reset my password?", "Can I migrate my domain?", "What is included in SSL?", "How do I restore backups?", "How do I create email accounts?", "How do I check bandwidth?", "Can I use a custom domain?", "How do I open a support ticket?", "Can I connect a database?", "How do I enable CDN?", "How do I cancel my plan?"};
    String[] faqAnswers = {"Go to the plan page and choose upgrade.", "Use the reset link on the login page.", "Yes, domain migration is available.", "SSL secures your website traffic.", "Use the backup restore tool in the dashboard.", "Go to the email section and add a mailbox.", "Check your usage metrics under analytics.", "Yes, add the domain in your dashboard.", "Click support and choose create ticket.", "Use the database tool to add your database.", "Enable CDN from the performance tab.", "Cancel your plan from the billing area."};

    List<FAQ> faqs = new ArrayList<>();
    if (faqRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        faqs.add(FAQ.builder()
                .question(faqQuestions[i])
                .answer(faqAnswers[i])
                .category(i % 2 == 0 ? "Billing" : "Setup")
                .build());
      }
      faqRepository.saveAll(faqs);
    } else {
      faqs = faqRepository.findAll();
    }
    System.out.println("FAQs count after seeding: " + faqRepository.count());

    String[] workflowNames = {"Deployment", "Backup", "Monitoring", "Scaling", "SSL renewal", "Database sync", "Cache clear", "Security scan", "Email sync", "DNS update", "Analytics import", "Resource cleanup"};
    List<WorkflowLog> workflowLogs = new ArrayList<>();
    if (workflowLogRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        workflowLogs.add(WorkflowLog.builder()
                .workflowName(workflowNames[i])
                .executionStatus(i % 3 == 0 ? "FAILED" : "SUCCESS")
                .executionDate(LocalDateTime.now().minusDays(i))
                .build());
      }
      workflowLogRepository.saveAll(workflowLogs);
    } else {
      workflowLogs = workflowLogRepository.findAll();
    }
    System.out.println("Workflow logs count after seeding: " + workflowLogRepository.count());

    List<AIResponse> aiResponses = new ArrayList<>();
    if (aiResponseRepository.count() == 0) {
      for (int i = 0; i < 12; i++) {
        aiResponses.add(AIResponse.builder()
                .prompt("Generate response for ticket: " + ticketSubjects[i])
                .response("AI answer for issue: " + ticketDescriptions[i])
                .provider("OpenAI")
                .confidenceScore(0.75 + i * 0.02)
                .ticket(tickets.get(i))
                .workflowLog(workflowLogs.get(i))
                .faq(faqs.get(i))
                .build());
      }
      aiResponseRepository.saveAll(aiResponses);
    }
    System.out.println("AI responses count after seeding: " + aiResponseRepository.count());

    System.out.println("✅ Default data inserted.");
  }

}
