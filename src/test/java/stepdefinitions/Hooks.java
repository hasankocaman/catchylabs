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
import utils.SparkReportCleanup;

public class Hooks {
    private WebDriver driver;
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final SparkReportCleanup sparkReportCleanup = new SparkReportCleanup();

    @Before
    public void setup(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        driver = DriverManager.getDriver();
        sparkReportCleanup.cleanupSparkReports();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            takeScreenshot(scenario);
            logger.error("Scenario failed: {}", scenario.getName());
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }

        // Driver'ı kapatma işlemi yoruma alındı
        // try {
        //     DriverManager.quitDriver();
        // } catch (Exception e) {
        //     logger.error("Error while quitting driver: {}", e.getMessage());
        // }
    }

    private void takeScreenshot(Scenario scenario) {
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

    protected void navigateToUrl(String url) {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        try {
            driver.get(url);
            logger.info("Navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Could not navigate to URL: {}", url);
            throw e;
        }
    }
}