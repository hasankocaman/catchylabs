package driver;

import config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;

public class DriverFactory {
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final Configuration config = Configuration.getInstance();

    public WebDriver createDriver(BrowserType browserType) {
        logger.info("Creating WebDriver instance for browser: {}", browserType);

        WebDriver driver;
        switch (browserType) {
            case CHROME:
                driver = createChromeDriver();
                break;
            case FIREFOX:
                driver = createFirefoxDriver();
                break;
            case EDGE:
                driver = createEdgeDriver();
                break;
            case SAFARI:
                driver = createSafariDriver();
                break;
            default:
                logger.error("Unsupported browser type: {}", browserType);
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        configureBrowser(driver);
        return driver;
    }

    private WebDriver createChromeDriver() {
        logger.debug("Configuring Chrome options");
        ChromeOptions options = new ChromeOptions();

        // Konfigürasyon dosyasından ayarları al
        boolean headless = config.getBooleanProperty("headless");
        if (headless) {
            options.addArguments("--headless=new");
        }

        // Diğer Chrome ayarları
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        logger.info("Creating Chrome WebDriver instance");
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver() {
        logger.debug("Configuring Firefox options");
        FirefoxOptions options = new FirefoxOptions();

        boolean headless = config.getBooleanProperty("headless");
        if (headless) {
            options.addArguments("-headless");
        }

        // Firefox ayarları
        options.addArguments("--start-maximized");

        logger.info("Creating Firefox WebDriver instance");
        return new FirefoxDriver(options);
    }

    private WebDriver createEdgeDriver() {
        logger.debug("Configuring Edge options");
        EdgeOptions options = new EdgeOptions();

        boolean headless = config.getBooleanProperty("headless");
        if (headless) {
            options.addArguments("--headless=new");
        }

        // Edge ayarları
        options.addArguments("--start-maximized");

        logger.info("Creating Edge WebDriver instance");
        return new EdgeDriver(options);
    }

    private WebDriver createSafariDriver() {
        logger.debug("Configuring Safari options");
        SafariOptions options = new SafariOptions();

        // Safari ayarları
        // Not: Safari headless modu desteklemez

        logger.info("Creating Safari WebDriver instance");
        return new SafariDriver(options);
    }

    private void configureBrowser(WebDriver driver) {
        try {
            // Varsayılan değerlerle birlikte config'den değerleri al
            int implicitWait = config.getIntProperty("implicit.wait", 10);
            int pageLoadTimeout = config.getIntProperty("page.load.timeout", 30);
            int scriptTimeout = config.getIntProperty("script.timeout", 20);

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));

            logger.info("Browser configured successfully with timeouts - Implicit: {}s, PageLoad: {}s, Script: {}s",
                    implicitWait, pageLoadTimeout, scriptTimeout);
        } catch (Exception e) {
            logger.error("Error configuring browser: {}", e.getMessage());
            throw new RuntimeException("Failed to configure browser", e);
        }
    }
}