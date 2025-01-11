package stepdefinitions;

import driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.time.Duration;

public class BaseSteps {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final Hooks hooks = new Hooks();
    protected final WebDriver driver = DriverManager.getDriver();

    protected void waitForPageLoad(int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Sayfa yüklenmesi tamamlandı");
        } catch (Exception e) {
            logger.error("Sayfa yüklenirken hata oluştu: {}", e.getMessage());
        }
    }

    protected void navigateToUrl(String url) {
        hooks.navigateToUrl(url);
    }

    protected String getCurrentUrl() {
        if (driver != null) {
            try {
                return driver.getCurrentUrl();
            } catch (Exception e) {
                logger.error("Mevcut URL alınırken hata: {}", e.getMessage());
                return null;
            }
        }
        logger.error("Mevcut URL alınırken driver null");
        return null;
    }

    protected String getPageTitle() {
        if (driver != null) {
            try {
                return driver.getTitle();
            } catch (Exception e) {
                logger.error("Sayfa başlığı alınırken hata: {}", e.getMessage());
                return null;
            }
        }
        logger.error("Sayfa başlığı alınırken driver null");
        return null;
    }
}