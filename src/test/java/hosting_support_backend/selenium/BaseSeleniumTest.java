package hosting_support_backend.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared setup for browser end-to-end tests.
 *
 * <p>These tests are tagged {@code selenium} and are excluded from the default
 * Maven build. They require a running application:
 * <ul>
 *   <li>backend on http://localhost:8081</li>
 *   <li>frontend dev server on http://localhost:5173</li>
 * </ul>
 *
 * Run them with:
 * <pre>{@code ./mvnw test -Pselenium}</pre>
 *
 * The frontend base URL can be overridden with
 * {@code -Dfrontend.base.url=http://localhost:3000}.
 * The browser opens visibly by default; use
 * {@code -Dselenium.headless=true} to run without a window.
 * A slow pace can be tuned with {@code -Dselenium.pause=2500}
 * (pause in milliseconds between steps, default 1200).
 */
@Tag("selenium")
abstract class BaseSeleniumTest {

    static final String BASE_URL =
            System.getProperty("frontend.base.url", "http://localhost:5173");

    private static final boolean HEADLESS =
            Boolean.parseBoolean(System.getProperty("selenium.headless", "false"));

    private static final long PAUSE_MS =
            Long.parseLong(System.getProperty("selenium.pause", "1200"));

    WebDriver driver;
    WebDriverWait wait;

    @BeforeAll
    static void setupDriverBinary() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
        if (HEADLESS) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        } else {
            options.addArguments("--window-size=1280,800");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Briefly stops so the browser actions are visible when running with a window. */
    void pause(String step) {
        System.out.println("  >> " + step);
        try {
            Thread.sleep(PAUSE_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}