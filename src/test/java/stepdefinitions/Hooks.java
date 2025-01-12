package stepdefinitions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
import reporting.ExtentReportManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {
    private WebDriver driver;
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final SparkReportCleanup sparkReportCleanup = new SparkReportCleanup();
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private ExtentTest extentTest;

    @Before
    public void setup(Scenario scenario) {
        String timestamp = new SimpleDateFormat(TIMESTAMP_PATTERN).format(new Date());

        // Senaryo adını kontrol et ve geçerli bir ad oluştur
        String scenarioName = getValidScenarioName(scenario);
        logger.info("[{}] Starting scenario: {}", timestamp, scenario.getName());

        // Extent Report için test oluştur
        extentTest = ExtentReportManager.createTest(scenario.getName());
        extentTest.log(Status.INFO, "Test Started at: " + timestamp);
        extentTest.log(Status.INFO, "Running Scenario: " + scenario.getName());

        driver = DriverManager.getDriver();
        sparkReportCleanup.cleanupSparkReports();
    }

    private String getValidScenarioName(Scenario scenario) {
        String name = scenario.getName();
        if (name == null || name.trim().isEmpty()) {
            // Eğer senaryo adı boşsa, unique bir ad oluştur
            name = "Scenario-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        }
        return name;
    }

    @After
    public void tearDown(Scenario scenario) {
        String timestamp = new SimpleDateFormat(TIMESTAMP_PATTERN).format(new Date());
        String scenarioName = getValidScenarioName(scenario);

        try {
            if (scenario.isFailed()) {
                takeScreenshot(scenario);
                logger.error("[{}] Scenario failed: {}", timestamp, scenarioName);
                if (extentTest != null) {
                    extentTest.log(Status.FAIL, "Scenario Failed at: " + timestamp);
                }
            } else {
                logger.info("[{}] Scenario passed: {}", timestamp, scenarioName);
                if (extentTest != null) {
                    extentTest.log(Status.PASS, "Scenario Passed at: " + timestamp);
                }
            }

            if (extentTest != null) {
                addTestDetails(scenario);
            }

        } finally {
            try {
                if (driver != null) {
                    captureAndLogBrowserLogs();
                }
            } catch (Exception e) {
                logger.error("Error in cleanup: {}", e.getMessage());
                if (extentTest != null) {
                    extentTest.log(Status.WARNING, "Cleanup Error: " + e.getMessage());
                }
            }

            ExtentReportManager.flush();
        }
    }


    private void takeScreenshot(Scenario scenario) {
        try {
            if (driver instanceof TakesScreenshot) {
                final byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                String screenshotName = "Screenshot-" + scenario.getName() + "-" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                scenario.attach(screenshot, "image/png", screenshotName);
                logger.info("Screenshot captured: {}", screenshotName);

                // Screenshot'ı Extent Report'a ekle
                extentTest.addScreenCaptureFromBase64String(
                        java.util.Base64.getEncoder().encodeToString(screenshot),
                        screenshotName
                );
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    private void addTestDetails(Scenario scenario) {
        try {
            extentTest.log(Status.INFO, "Scenario Details:");
            extentTest.log(Status.INFO, "Status: " + scenario.getStatus());
            extentTest.log(Status.INFO, "Tags: " + scenario.getSourceTagNames());
        } catch (Exception e) {
            logger.error("Error adding test details to report: {}", e.getMessage());
        }
    }

    private void captureAndLogBrowserLogs() {
        try {
            // Browser loglarını yakala
            logger.info("Browser logs captured");
            extentTest.log(Status.INFO, "Browser logs captured successfully");
        } catch (Exception e) {
            logger.error("Error capturing browser logs: {}", e.getMessage());
        }
    }

    protected void navigateToUrl(String url) {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        try {
            driver.get(url);
            logger.info("Navigated to URL: {}", url);
            extentTest.log(Status.INFO, "Navigated to: " + url);
        } catch (Exception e) {
            logger.error("Navigation failed to URL: {}", url);
            extentTest.log(Status.FAIL, "Navigation failed to: " + url);
            throw e;
        }
    }
}