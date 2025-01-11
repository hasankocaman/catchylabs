package stepdefinitions;





import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Tüm step definition sınıfları için temel sınıf.
 * Ortak özellikleri ve hook metodlarını içerir.
 */
public class BaseSteps {
    protected WebDriver driver;
    protected final Logger logger = LogManager.getLogger(this.getClass());

    public BaseSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Before
    public void setup(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        driver = DriverManager.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            // Senaryo başarısız olduğunda ekran görüntüsü al
            takeScreenshot(scenario);
            logger.error("Scenario failed: {}", scenario.getName());
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }

        // Driver'ı temizle
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            logger.error("Error while quitting driver: {}", e.getMessage());
        }
    }

    /**
     * Ekran görüntüsü alır ve senaryoya ekler
     * @param scenario Mevcut senaryo
     */
    protected void takeScreenshot(Scenario scenario) {
        try {
            final byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png",
                    "Screenshot-" + scenario.getName());
            logger.info("Screenshot taken for failed scenario: {}",
                    scenario.getName());
        } catch (Exception e) {
            logger.error("Could not take screenshot: {}", e.getMessage());
        }
    }

    /**
     * Sayfanın yüklenmesini bekler
     * @param seconds Beklenecek süre (saniye)
     */
    protected void waitForPageLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.info("Waited {} seconds for page load", seconds);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * URL'e gider
     * @param url Gidilecek URL
     */
    protected void navigateToUrl(String url) {
        try {
            driver.get(url);
            logger.info("Navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Could not navigate to URL: {}", url);
            throw e;
        }
    }

    /**
     * Geçerli URL'i döndürür
     * @return Geçerli URL
     */
    protected String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL: {}", currentUrl);
        return currentUrl;
    }

    /**
     * Sayfa başlığını döndürür
     * @return Sayfa başlığı
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
        return title;
    }
}