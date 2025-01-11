package driver;


import config.ConfigurationManager;
import org.openqa.selenium.WebDriver;

// DriverManager.java - Singleton Pattern
public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final DriverFactory driverFactory = new DriverFactory();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            BrowserType browserType = BrowserType.valueOf(
                    ConfigurationManager.getInstance().getProperty("browser").toUpperCase()
            );
            driver.set(driverFactory.createDriver(browserType));
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}