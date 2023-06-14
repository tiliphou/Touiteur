package fr.uha.ensisa.antidemo.IT;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import fr.uha.ensisa.eco.metrologie.extension.EcoExtension;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDocker;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoDockerContainer;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoGatling;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoMonitor;
import fr.uha.ensisa.eco.metrologie.extension.annotations.EcoWebDriver;

@EcoDocker(network = "metrologie-network", url = "$DOCKER_HOST$")
@EcoDockerContainer(id = "$NGINX_ID$", port=8082)
@EcoMonitor(containerId = "$APP_ID$")
@EcoMonitor(containerId = "$MYSQL_ID$")
@EcoGatling(userCount = 20)
@EcoWebDriver(remote = true)
@ExtendWith(EcoExtension.class)
public class AntidemoIT {
    
    @RepeatedTest(3)
    void testIndex(WebDriver driver) {
        driver.get("/");
        System.out.println("Hello from testIndex");
        System.out.println(driver.getCurrentUrl());
    }
}
