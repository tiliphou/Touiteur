package fr.uha.ensisa.antidemo.IT;

import org.junit.jupiter.api.RepeatedTest;
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
    private Integer NOMBRE_ARTICLE_TEST = 2;
    //en secondes
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
}
