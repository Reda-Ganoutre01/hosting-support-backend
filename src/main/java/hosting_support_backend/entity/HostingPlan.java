package hosting_support_backend.entity;


import jakarta.persistence.Entity;

@Entity
public class HostingPlan {
    long id;
    String name;
    String description;
    Double price;
    Integer storage;
    Integer bandwidth;
    Integer emailAccounts;
    Boolean sslIncluded;
}
