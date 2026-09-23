package hosting_support_backend.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end tests for the login page.
 *
 * <p>Uses the seeded admin account: admin@vala.com / 12345678.
 */
class LoginPageSeleniumTest extends BaseSeleniumTest {

    @Test
    void loginForm_isDisplayed() {
        pause("Ouverture de la page de connexion");
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        pause("Champ email visibles");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Connexion à votre compte"),
                "The login card title should be displayed");
        assertTrue(pageSource.contains("Se connecter"),
                "The submit button label should be displayed");
        pause("Formulaire de connexion vérifié");
    }

    @Test
    void adminLogin_navigatesToDashboard() {
        pause("Ouverture de la page de connexion");
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        pause("Saisie de l'adresse email");
        driver.findElement(By.id("email")).sendKeys("admin@vala.com");
        pause("Saisie du mot de passe");
        driver.findElement(By.id("password")).sendKeys("12345678");
        pause("Clic sur le bouton Se connecter");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "Admin should be redirected to the dashboard after login");
        pause("Redirection vers le dashboard effectuée");
    }

    @Test
    void wrongPassword_keepsUserOnLoginPage() {
        pause("Ouverture de la page de connexion");
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        driver.findElement(By.id("email")).sendKeys("admin@vala.com");
        driver.findElement(By.id("password")).sendKeys("definitely-wrong");
        pause("Soumission des identifiants incorrects");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "A failed login should stay on the login page");
        pause("Connexion refusée, toujours sur la page de connexion");
    }
}