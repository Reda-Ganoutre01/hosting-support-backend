package hosting_support_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HostingSupportBackendApplication {

	public static void main(String[] args) {
		String host = System.getenv("DB_HOST");
		if (host == null || host.isBlank()) host = System.getenv("MYSQLHOST");
		String dbName = System.getenv("DB_NAME");
		if (dbName == null || dbName.isBlank()) dbName = System.getenv("MYSQLDATABASE");
		System.out.println(">>> Connecting to MySQL at host=" + (host == null ? "localhost (no DB env set!)" : host)
				+ ", database=" + (dbName == null ? "hosting_support_db" : dbName));
		SpringApplication.run(HostingSupportBackendApplication.class, args);
	}

}
