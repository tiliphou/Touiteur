package fr.uha.ensisa.antidemo.IT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import java.util.*;

import fr.uha.ensisa.eco.metrologie.extension.EcoExtension;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDocker;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDockerContainer;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoGatling;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoMonitor;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoWebDriver;

@EcoDocker(network = "metrologie-network", url = "$DOCKER_HOST$")
@EcoDockerContainer(id = "anti-demo-proxy-1", port=8082)
@EcoMonitor(containerId = "anti-demo-app-1")
@EcoMonitor(containerId = "anti-demo-db-1")
@EcoGatling(userCount = 20)
@EcoWebDriver(remote = true)
@ExtendWith(EcoExtension.class)
public class AntidemoIT {
    private Integer NOMBRE_ARTICLE_TEST = 2; //en secondes
    private int TEMPS_PAR_ARTICLE = 10;
    private int TEMPS_RECHERCHE_ARTICLE = 5;
    
    @RepeatedTest(3)
    void testIndex(WebDriver wb) {
        JavascriptExecutor js = (JavascriptExecutor)wb;
        wb.get("/");
        int nb;
        try {
            Random rnd = new Random();
            js.executeScript("window.scrollBy(-2000,0)", "");
            Thread.sleep(TEMPS_RECHERCHE_ARTICLE*1000);
            for(int i = 0; i< NOMBRE_ARTICLE_TEST; i++){

                List<WebElement> articles = wb.findElements(By.className("danger-btn"));
                nb=rnd.nextInt(articles.size());
                articles.get(nb).click();
                Thread.sleep(TEMPS_PAR_ARTICLE*300);
                js.executeScript("window.scrollTo(0,document.body.scrollHeight)", "");
                Thread.sleep(TEMPS_PAR_ARTICLE*500);
                js.executeScript("window.scrollTo(0,0)", "");
                Thread.sleep(TEMPS_PAR_ARTICLE*200);
                wb.get("/");
                js.executeScript("window.scrollBy(-2500,0)", "");
                Thread.sleep(TEMPS_RECHERCHE_ARTICLE*1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(3)
    @DisplayName("Test search")
    void testSearch(WebDriver driver) {
        // go to index page
        driver.get("/index");

        // search article 'Exemple - 997'
        WebElement searchInput = driver.findElement(By.id("search-input"));
        String articleName = "Exemple - 997";
        searchInput.sendKeys(articleName);

        // assertions ...
    }

    @Test
    @DisplayName("Test account")
    void testAccount(WebDriver driver) {
        // TODO: repeat this test 3 times like the others
        // Steps:
        //  1 - if registered, go to /index and click on account button
        //  2 - click on logout

        // go to index page
        driver.get("/index");

        // click on sign up
        WebElement signUpButton = driver.findElement(By.id("register-btn"));
        signUpButton.click();

        // create an account
        String firstName = "Maxime" + System.currentTimeMillis();
        String lastName = "Kuhn" + System.currentTimeMillis();
        String email = String.format("maxime%s.kuhn@fakemail.com", String.valueOf(System.currentTimeMillis()));
        String password = "password";
        this.createAccount(driver, firstName, lastName, email, password);

        // login
        this.login(driver, email, password);
    }

    @RepeatedTest(3)
    @DisplayName("Test read article")
    void testReadArticle(WebDriver driver) {
        // go to index page
        driver.get("/index");

        // get a read more button
        List<WebElement> readMoreButtons = driver.findElements(By.className("danger-btn"));
        WebElement readMoreButton = readMoreButtons.get(0);
        readMoreButton.click();

        // scroll down
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    @RepeatedTest(3)
    @DisplayName("Test edit post")
    void testEditPost(WebDriver driver) {
        // go to registration page
        driver.get("/registration");

        // create account and login
        String firstName = "Frédéric" + System.currentTimeMillis();
        String lastName = "Fondement" + System.currentTimeMillis();
        String email = String.format("frederic%s.fondement@fakemail.com", String.valueOf(System.currentTimeMillis()));
        String password = "password";
        this.createAccount(driver, firstName, lastName, email, password);
        this.login(driver, email, password);

        // go to account
        WebElement accountButton = driver.findElement(By.id("account-btn"));
        accountButton.click();

        // click on an Edit button
        List<WebElement> buttons = driver.findElements(By.className("submit-btn"));
        for (WebElement button : buttons) {
            if (button.getText().equals("Edit")) {
                button.click();
                break;
            }
        }

        // modify content
        WebElement contentInput = driver.findElement(By.id("content-input"));
        contentInput.sendKeys("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        // submit
        buttons = driver.findElements(By.className("submit-btn"));
        for (WebElement button : buttons) {
            if (button.getText().contains("Update")) {
                button.click();
                break;
            }
        }
    }

    private void createAccount(WebDriver driver, String firstName, String lastName, String email, String password) {
        WebElement firstNameInput = driver.findElement(By.id("firstName"));
        WebElement lastNameInput = driver.findElement(By.id("lastName"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement passwordConfirmationInput = driver.findElement(By.id("passwordConfirmation"));
        WebElement submitButton = driver.findElement(By.className("submit-btn"));

        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        passwordConfirmationInput.sendKeys(password);
        submitButton.click();
    }

    private void login(WebDriver driver, String email, String password) {
        WebElement emailInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.id("login-submit"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        submitButton.click();
    }
}
