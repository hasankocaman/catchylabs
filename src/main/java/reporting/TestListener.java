package reporting;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import driver.DriverManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import reporting.ExtentReportManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Test yürütme sürecini dinleyen ve raporlayan listener sınıfı.
 * Cucumber event'lerini yakalar ve ExtentReports'a iletir.
 */
public class TestListener implements ConcurrentEventListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private final ExtentReports extent = ExtentReportManager.getInstance();
    private static final Map<String, ExtentTest> featureMap = new HashMap<>();
    private static final Map<String, ExtentTest> scenarioMap = new HashMap<>();
    private static final ThreadLocal<ExtentTest> currentScenario = new ThreadLocal<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Test başlamadan önce
        publisher.registerHandlerFor(TestRunStarted.class, this::testRunStarted);

        // Feature başlamadan önce
        publisher.registerHandlerFor(TestCaseStarted.class, this::testCaseStarted);

        // Step başlamadan önce
        publisher.registerHandlerFor(TestStepStarted.class, this::testStepStarted);

        // Step bittikten sonra
        publisher.registerHandlerFor(TestStepFinished.class, this::testStepFinished);

        // Senaryo bittikten sonra
        publisher.registerHandlerFor(TestCaseFinished.class, this::testCaseFinished);

        // Test koşumu bittikten sonra
        publisher.registerHandlerFor(TestRunFinished.class, this::testRunFinished);
    }

    private void testRunStarted(TestRunStarted event) {
        logger.info("Test execution started");
    }

    private void testCaseStarted(TestCaseStarted event) {
        String featureName = event.getTestCase().getUri().toString();
        String scenarioName = event.getTestCase().getName();

        // Feature için ExtentTest oluştur veya mevcut olanı al
        ExtentTest feature = featureMap.computeIfAbsent(featureName,
                k -> extent.createTest("Feature: " + getFeatureName(featureName)));

        // Senaryo için ExtentTest oluştur
        ExtentTest scenario = feature.createNode("Scenario: " + scenarioName);
        scenarioMap.put(getScenarioId(event.getTestCase()), scenario);
        currentScenario.set(scenario);

        logger.info("Starting scenario: {}", scenarioName);
    }

    private void testStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            String stepText = step.getStep().getText();
            currentScenario.get().info("Starting step: " + stepText);
            logger.info("Starting step: {}", stepText);
        }
    }

    private void testStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            String stepText = step.getStep().getText();
            Status status = getStatus(event.getResult().getStatus());

            ExtentTest currentTest = currentScenario.get();

            switch (status) {
                case PASS:
                    currentTest.pass("Step passed: " + stepText);
                    break;
                case FAIL:
                    String errorMessage = event.getResult().getError().getMessage();
                    currentTest.fail("Step failed: " + stepText + "\nError: " + errorMessage);
                    // Screenshot al
                    takeScreenshot(currentTest);
                    break;
                case SKIP:
                    currentTest.skip("Step skipped: " + stepText);
                    break;
                default:
                    currentTest.warning("Step status unknown: " + stepText);
            }

            logger.info("Step finished: {} with status: {}", stepText, status);
        }
    }


    private void testCaseFinished(TestCaseFinished event) {
        String scenarioId = getScenarioId(event.getTestCase());
        ExtentTest scenario = scenarioMap.get(scenarioId);

        // Cucumber Status'ü kullan
        if (event.getResult().getStatus() == io.cucumber.plugin.event.Status.FAILED) {
            scenario.fail("Scenario failed");
            if (event.getResult().getError() != null) {
                scenario.fail(event.getResult().getError());
            }
        }

        logger.info("Finished scenario: {} with status: {}",
                event.getTestCase().getName(), event.getResult().getStatus());

        scenarioMap.remove(scenarioId);
        currentScenario.remove();
    }
    private void testRunFinished(TestRunFinished event) {
        extent.flush();
        featureMap.clear();
        scenarioMap.clear();
        logger.info("Test execution finished");
    }

    private Status getStatus(io.cucumber.plugin.event.Status cucumberStatus) {
        switch (cucumberStatus) {
            case PASSED:
                return Status.PASS;
            case FAILED:
                return Status.FAIL;
            case SKIPPED:
                return Status.SKIP;
            case PENDING:
                return Status.WARNING;
            case UNUSED:
                return Status.SKIP;
            case AMBIGUOUS:
                return Status.WARNING;
            default:
                return Status.WARNING;
        }
    }

    private String getFeatureName(String featurePath) {
        try {
            if (featurePath != null && featurePath.contains("/")) {
                return featurePath.substring(featurePath.lastIndexOf("/") + 1)
                        .replace(".feature", "");
            }
            return featurePath != null ? featurePath : "Unknown Feature";
        } catch (Exception e) {
            logger.error("Error getting feature name: {}", e.getMessage());
            return "Unknown Feature";
        }
    }
    private String getScenarioId(TestCase testCase) {
        return testCase.getId().toString();
    }

    private void takeScreenshot(ExtentTest test) {
        try {
            WebDriver driver = getDriverInstance();
            if (driver != null && driver instanceof TakesScreenshot) {
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                test.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                logger.info("Screenshot captured successfully");
            } else {
                logger.warn("Could not capture screenshot - driver is null or not instance of TakesScreenshot");
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    private WebDriver getDriverInstance() {
        try {
            return DriverManager.getDriver();
        } catch (Exception e) {
            logger.error("Could not get WebDriver instance: {}", e.getMessage());
            return null;
        }
    }
}