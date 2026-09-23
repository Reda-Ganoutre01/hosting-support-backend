package hosting_support_backend.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end tests for the public home page.
 */
class HomePageSeleniumTest extends BaseSeleniumTest {

    @Test
    void homePage_loadsWithNavigationBar() {
        pause("Ouverture de la page d'accueil");
        driver.get(BASE_URL + "/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='Vala Logo']")));
        pause("Logo Vala affiché dans la barre de navigation");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Hébergement Web au Maroc"),
                "The home page hero should contain the French headline");
        pause("Contenu de l'accueil vérifié");
    }

    @Test
    void homePage_navigationLeadsToLogin() {
        pause("Ouverture de la page d'accueil");
        driver.get(BASE_URL + "/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='Vala Logo']")));
        pause("Recherche du lien Mon Compte");
        driver.findElement(By.linkText("Mon Compte")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "Clicking 'Mon Compte' should open the login page");
        pause("Redirection vers la page de connexion effectuée");
    }
}