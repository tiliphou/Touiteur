package fr.uha.ensisa.antidemo.it;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.uha.ensisa.eco.metrologie.extension.EcoExtension;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDocker;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDockerContainer;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoGatling;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoMonitor;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoWebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@EcoDocker(network = "metrology") //, clean = false)
@EcoDockerContainer(id = "anti-demo-proxy-1")
@EcoMonitor(containerId = "anti-demo-app-1")
@EcoMonitor(containerId = "anti-demo-db-1")
@EcoWebDriver(remote = true)
@EcoGatling(userCount = 100, rampDuration = 10)
@ExtendWith(EcoExtension.class)
public class PremierTest {

    //variable pour tout les tests
    private CharSequence email;
    private Integer NOMBRE_ARTICLE_TEST =2;
    //en secondes
    private int TEMPS_PAR_ARTICLE = 10;
    private int TEMPS_RECHERCHE_ARTICLE = 5;

    private Random rnd = new Random();

    //créer un compte
    // !!! attention !!!
    // ne revoie pas à l'index après la création du compte
    private void createAcc(WebDriver wb){
        List<WebElement> acc = wb.findElements(By.id("account-btn"));
        if (!acc.isEmpty()) {
            wb.get("/logout");
            wb.get("/");
        }

        wb.findElement(By.id("register-btn")).click();
        List<WebElement> inputs = wb.findElements(By.className("form-input"));
        CharSequence firstname;
        CharSequence lastname;
        CharSequence password;

        String generatedString = new Random().ints('a', 'z').limit(8)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        firstname = "aaaa";
        lastname = "aaaa";
        this.email = generatedString+"@aa";
        password = "aaaa";



        inputs.get(0).sendKeys(firstname);
        inputs.get(1).sendKeys(lastname);
        inputs.get(2).sendKeys(this.email);
        inputs.get(3).sendKeys(password);
        inputs.get(4).sendKeys(password);

        wb.findElement(By.className("submit-btn")).click();
    }

    private void login(WebDriver wb){
        if (!wb.getCurrentUrl().endsWith("/login")) {
            wb.findElement(By.id("login-btn")).click();
        }

        WebElement username = wb.findElement(By.id("username"));
        WebElement pwd = wb.findElement(By.id("password"));

        username.sendKeys(this.email);
        pwd.sendKeys("aaaa");

        wb.findElement(By.id("login-submit")).submit();
    }

    @Test
    public void testAccountCreation(WebDriver wb) throws IOException  {
        wb.get("/");

        //création d'un compte
        this.createAcc(wb);

        //vérification que la création à été éffectuée
        assertEquals("/login", wb.getCurrentUrl());
    }

    @Test
    public void testAccountLogin(WebDriver wb)  {
        wb.get("/");

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc(wb);
        }

        //se conecter
        this.login(wb);

        new WebDriverWait(wb, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(By.id("account-btn")));
    }


    @Test
    public void testModifyPost(WebDriver wb)  {
        wb.get("/");

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc(wb);
        }

        //se conecter
        this.login(wb);

        new WebDriverWait(wb, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(By.id("account-btn")))
            .click();

        List<WebElement> buttons = wb.findElements(By.className("submit-btn"));
        for(WebElement comp : buttons){
            String text=comp.getText();
            if(text.equals("Edit")){
                comp.click();
                break;
            }
        }

        WebElement textArea = wb.findElement(By.id("content-input"));

        textArea.click();
        textArea.sendKeys("aAa");
        buttons = wb.findElements(By.className("submit-btn"));
        for(WebElement comp : buttons){
            String text=comp.getText();
            if(text.equals("Update post")){
                //comp.click();
                break;
            }
        }
    }

    @Test
    public void testUploadImage(WebDriver wb) throws MalformedURLException, IOException  {
        wb.get("/");

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc(wb);
        }

        //se connecter
        this.login(wb);

        new WebDriverWait(wb, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(By.id("account-btn")))
            .click();

        List<WebElement> buttons = wb.findElements(By.className("side-bar-text"));
        for(WebElement comp : buttons){
            String text=comp.getText();
            if(text.equals("Images")){
                comp.click();
                break;
            }
        }

        WebElement upload = wb.findElement(By.id("image-input"));
        File img = new File("sample-image.jpg").getAbsoluteFile();
        if (! img.exists()) {
            try(InputStream stream = new URL("http://2.bp.blogspot.com/-4znwGoQFzNU/VjbzqIPnRHI/AAAAAAAAEIM/2o5X9BnDc2w/s320/logo.jpg").openStream()) {
                Files.copy(stream, img.toPath());
            }
        }
        upload.sendKeys(img.getAbsolutePath());
        wb.findElement(By.id("image-upload-form")).submit();
    }

    @RepeatedTest(3)
    public void papillonnage(WebDriver wb){
        JavascriptExecutor js = (JavascriptExecutor) wb;
        wb.get("/");
        if(NOMBRE_ARTICLE_TEST == null)
            NOMBRE_ARTICLE_TEST = rnd.nextInt(10)+1;
        int nb;
        try {

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
}

