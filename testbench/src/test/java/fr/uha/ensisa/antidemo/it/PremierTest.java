package fr.uha.ensisa.antidemo.it;

import static org.testng.Assert.*;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class PremierTest {

    public static final String NOM_FICHIER_HAR = "testPapillonage.har";
    private final String BASEADDRESS = "http://localhost:8082";

    //variable pour tout les tests
    private CharSequence email;

    private Integer NOMBRE_TEST =2;
    //en secondes
    private int TEMPS_INTER_TEST=30;
    private Integer NOMBRE_ARTICLE_TEST =2;
    //en secondes
    private int TEMPS_PAR_ARTICLE = 10;
    private int TEMPS_RECHERCHE_ARTICLE = 5;

    private WebDriver wb;
    private BrowserMobProxyServer proxy;

    @BeforeClass
    private void setupChromeDriver() throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", Utils.getChromeDriver().getAbsolutePath());
    }

    @BeforeMethod
    public void startChrome() {

        //prerequis pour enregistrement pour gatling

        proxy = new BrowserMobProxyServer();
        proxy.start();
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.PROXY,seleniumProxy);
        capabilities.acceptInsecureCerts();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();

        captureTypes.addAll(CaptureType.getCookieCaptureTypes());
        captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
        captureTypes.addAll(CaptureType.getRequestCaptureTypes());
        captureTypes.addAll(CaptureType.getResponseCaptureTypes());



        proxy.setHarCaptureTypes(captureTypes);
        proxy.newHar("testPapillonage");

        //test selenium
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(capabilities);
        chromeOptions.setProxy(seleniumProxy);
        chromeOptions.setAcceptInsecureCerts(true);
        wb = new ChromeDriver(chromeOptions);
    }

    @AfterMethod
    public void stopChrome() {
        if (this.wb != null) {
            this.wb.close();
            this.wb = null;
        }
    }

    //créer un compte
    // !!! attention !!!
    // ne revoie pas à l'index après la création du compte
    private void createAcc(){
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

    private void login(){
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
    public void testAccountCreation() throws IOException  {
        wb.get(this.BASEADDRESS);

        //création d'un compte
        this.createAcc();

        //vérification que la création à été éffectuer
        assertEquals(this.BASEADDRESS+"/login", wb.getCurrentUrl());
    }

    @Test
    public void testAccountLogin()  {
        wb.get(this.BASEADDRESS);

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc();
        }

        //se conecter
        this.login();

        new WebDriverWait(wb, 5)
            .until(ExpectedConditions.elementToBeClickable(By.id("account-btn")));
    }


    @Test
    public void testModifyPost()  {
        wb.get(this.BASEADDRESS);

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc();
        }

        //se conecter
        this.login();

        new WebDriverWait(wb, 5)
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
    public void testUploadImage() throws MalformedURLException, IOException  {
        wb.get(this.BASEADDRESS);

        //si pas d'identifiant de compte stocké créer un compte
        if(this.email==null){
            this.createAcc();
        }

        //se connecter
        this.login();

        new WebDriverWait(wb, 5)
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

    @Test
    public void testPapillonage()  {

        Random rnd = new Random();
        if(NOMBRE_TEST == null)
            NOMBRE_TEST = rnd.nextInt(10)+1;
        System.out.println("Nombre de tests : " + NOMBRE_TEST);

        //formateur pour timestamp de synchronisation avec la mesure
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        //premier test
        System.out.println("Debut test "+"1"+" à "+dtf.format(LocalDateTime.now()) + " (" + System.currentTimeMillis() + ')');
        papillonage(wb,rnd);
        System.out.println("Fin test "+"1"+" à "+dtf.format(LocalDateTime.now()) + " (" + System.currentTimeMillis() + ')');
        try {
            Thread.sleep(TEMPS_INTER_TEST*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //on enregistre le HAR du premier test pour Gatling
        Har har = proxy.getHar();
        File harFile = new File(NOM_FICHIER_HAR).getAbsoluteFile();
        try {
            har.writeTo(harFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //le reste des tests sans enregistrements
        for(int i=0;i<NOMBRE_TEST-1;i++){
            System.out.println("Debut test "+i+2+" à "+dtf.format(LocalDateTime.now()) + " (" + System.currentTimeMillis() + ')');
            papillonage(wb,rnd);
            System.out.println("Fin test "+i+2+" à "+dtf.format(LocalDateTime.now()) + " (" + System.currentTimeMillis() + ')');
            try {
                Thread.sleep(TEMPS_INTER_TEST*1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    private void papillonage(WebDriver wb,Random rnd){
        JavascriptExecutor js = (JavascriptExecutor) wb;
        wb.get(this.BASEADDRESS);
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
                wb.get(this.BASEADDRESS);
                js.executeScript("window.scrollBy(-2500,0)", "");
                Thread.sleep(TEMPS_RECHERCHE_ARTICLE*1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

