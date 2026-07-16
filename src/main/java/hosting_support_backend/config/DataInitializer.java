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
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.entity.enums.SenderType;
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

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
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

    List<User> users = new ArrayList<>();
    if (userRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        users.add(userRepository.save(
            User.builder()
                .userName("redux" + i)
                .fullName("redux Gantt " + i)
                .email("reda" + i + "@gmail.com")
                .password("123456")
                .role(Role.USER)
                .build()));
      }
    } else {
      users = userRepository.findAll();
    }

    List<HostingPlan> plans = new ArrayList<>();
    if (hostingPlanRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        plans.add(hostingPlanRepository.save(
            HostingPlan.builder()
                .name("Plan " + i)
                .description("Plan " + i + " description")
                .price(4.99 + i)
                .build()));
      }
    } else {
      plans = hostingPlanRepository.findAll();
    }

    List<HostingAccount> accounts = new ArrayList<>();
    if (hostingAccountRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        accounts.add(hostingAccountRepository.save(
            HostingAccount.builder()
                .domainName("reda" + i + ".com")
                .user(users.get((i - 1) % users.size()))
                .hostingPlan(plans.get((i - 1) % plans.size()))
                .build()));
      }
    } else {
      accounts = hostingAccountRepository.findAll();
    }

    List<Ticket> tickets = new ArrayList<>();
    if (ticketRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        tickets.add(ticketRepository.save(
            Ticket.builder()
                .subject("Support ticket " + i)
                .description("Please help with hosting issue number " + i + ".")
                .user(users.get((i - 1) % users.size()))
                .build()));
      }
    } else {
      tickets = ticketRepository.findAll();
    }

    if (notificationRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        notificationRepository.save(
            Notification.builder()
                .title("Notification " + i)
                .message("Your hosting account has an update for item " + i + ".")
                .read(i % 3 == 0)
                .user(users.get((i - 1) % users.size()))
                .hostingAccount(accounts.get((i - 1) % accounts.size()))
                .build());
      }
    }

    if (messageRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        messageRepository.save(
            Message.builder()
                .content("This is message " + i + " for ticket conversation.")
                .sender(i % 2 == 0 ? SenderType.USER : SenderType.ADMIN)
                .ticket(tickets.get((i - 1) % tickets.size()))
                .user(users.get((i - 1) % users.size()))
                .build());
      }
    }

    List<FAQ> faqs = new ArrayList<>();
    if (faqRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        faqs.add(faqRepository.save(
            FAQ.builder()
                .question("How do I use feature " + i + "?")
                .answer("Use the feature " + i + " by following these steps.")
                .category("General")
                .build()));
      }
    } else {
      faqs = faqRepository.findAll();
    }

    List<WorkflowLog> workflowLogs = new ArrayList<>();
    if (workflowLogRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        workflowLogs.add(workflowLogRepository.save(
            WorkflowLog.builder()
                .workflowName("Workflow " + i)
                .executionStatus(i % 2 == 0 ? "SUCCESS" : "FAILED")
                .build()));
      }
    } else {
      workflowLogs = workflowLogRepository.findAll();
    }

    if (aiResponseRepository.count() == 0) {
      for (int i = 1; i <= 12; i++) {
        aiResponseRepository.save(
            AIResponse.builder()
                .prompt("Generate response for ticket " + i)
                .response("This is an automated response for ticket " + i + ".")
                .provider("OpenAI")
                .confidenceScore(0.85 + (i * 0.01))
                .ticket(tickets.get((i - 1) % tickets.size()))
                .workflowLog(workflowLogs.get((i - 1) % workflowLogs.size()))
                .faq(faqs.get((i - 1) % faqs.size()))
                .build());
      }
    }

    System.out.println("✅ Default data inserted.");
  }

}
