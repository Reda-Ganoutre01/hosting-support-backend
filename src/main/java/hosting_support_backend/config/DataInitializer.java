package hosting_support_backend.config;

import hosting_support_backend.entity.HostingAccount;
import hosting_support_backend.entity.HostingPlan;
import hosting_support_backend.entity.Ticket;
import hosting_support_backend.entity.User;
import hosting_support_backend.entity.enums.Role;
import hosting_support_backend.repository.HostingAccountRepository;
import hosting_support_backend.repository.HostingPlanRepository;
import hosting_support_backend.repository.TicketRepository;
import hosting_support_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final HostingPlanRepository hostingPlanRepository;
  private final HostingAccountRepository hostingAccountRepository;
  private final TicketRepository ticketRepository;
  // private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {

    // user
    User user = null;
    if (userRepository.count() == 0) {
      user = userRepository.save(
          User.builder()
              .firstName("Reda")
              .lastName("Gantt")
              .email("reda@gmail.com")
              .password("123456")
              .role(Role.USER)
              .build());
    } else {
      user = userRepository.findAll().get(0);
    }

    // Hosting Plan
        HostingPlan plan = null;
        if (hostingPlanRepository.count() == 0) {
            plan = hostingPlanRepository.save(
                    HostingPlan.builder()
                            .name("Basic")
                            .price(4.99)
                            .build()
            );
        } else {
            plan = hostingPlanRepository.findAll().get(0);
        }

         // Hosting Account
        HostingAccount account = null;
        if (hostingAccountRepository.count() == 0) {
            account = hostingAccountRepository.save(
                    HostingAccount.builder()
                            .domainName("reda.com")
                            .user(user)
                            .hostingPlan(plan)
                            .build()
            );
        } else {
            account = hostingAccountRepository.findAll().get(0);
        }

        // Ticket
        if (ticketRepository.count() == 0) {
            ticketRepository.save(
                    Ticket.builder()
                            .subject("My first ticket")
                            .description("Need help with hosting.")
                            .user(user)
                            .build()
            );
        }

        System.out.println("✅ Default data inserted.");
  }

}
