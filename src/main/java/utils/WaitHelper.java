package utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Selenium WebDriver için bekleme işlemlerini yöneten yardımcı sınıf.
 * Çeşitli bekleme stratejilerini ve utility metodlarını içerir.
 */
public class WaitHelper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final FluentWait<WebDriver> fluentWait;
    private final Logger logger = LogManager.getLogger(WaitHelper.class);
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int POLLING_INTERVAL = 500;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Element görünür olana kadar bekler
     * @param element Beklenecek element
     * @return WebElement
     */
    public WebElement waitForElementVisible(WebElement element) {
        try {
            logger.info("Waiting for element to be visible: {}", element);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element not visible after {} seconds: {}", DEFAULT_TIMEOUT, element);
            throw e;
        }
    }

    /**
     * Element görünür olana kadar bekler
     * @param locator Element locator'ı
     * @return WebElement
     */
    public WebElement waitForElementVisible(By locator) {
        try {
            logger.info("Waiting for element to be visible: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible after {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }

    /**
     * Element tıklanabilir olana kadar bekler
     * @param element Beklenecek element
     * @return WebElement
     */
    public WebElement waitForElementClickable(WebElement element) {
        try {
            logger.info("Waiting for element to be clickable: {}", element);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element not clickable after {} seconds: {}", DEFAULT_TIMEOUT, element);
            throw e;
        }
    }

    /**
     * Element tıklanabilir olana kadar bekler
     * @param locator Element locator'ı
     * @return WebElement
     */
    public WebElement waitForElementClickable(By locator) {
        try {
            logger.info("Waiting for element to be clickable: {}", locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable after {} seconds: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }

    /**
     * Element görünmez olana kadar bekler
     * @param element Beklenecek element
     */
    public boolean waitForElementInvisible(WebElement element) {
        try {
            logger.info("Waiting for element to be invisible: {}", element);
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element still visible after {} seconds: {}", DEFAULT_TIMEOUT, element);
            throw e;
        }
    }

    /**
     * Text element içinde görünür olana kadar bekler
     * @param element Text'in aranacağı element
     * @param text Aranacak text
     * @return boolean
     */
    public boolean waitForTextPresent(WebElement element, String text) {
        try {
            logger.info("Waiting for text '{}' to be present in element: {}", text, element);
            return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            logger.error("Text '{}' not present in element after {} seconds: {}", text, DEFAULT_TIMEOUT, element);
            throw e;
        }
    }

    /**
     * Sayfa yüklenene kadar bekler
     */
    public void waitForPageLoad() {
        try {
            logger.info("Waiting for page to load");
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            logger.error("Page not loaded after {} seconds", DEFAULT_TIMEOUT);
            throw e;
        }
    }

    /**
     * AJAX çağrıları tamamlanana kadar bekler
     */
    public void waitForAjaxComplete() {
        try {
            logger.info("Waiting for AJAX calls to complete");
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0"));
        } catch (TimeoutException e) {
            logger.error("AJAX calls not completed after {} seconds", DEFAULT_TIMEOUT);
            throw e;
        }
    }

    /**
     * Angular işlemleri tamamlanana kadar bekler
     */
    public void waitForAngularLoad() {
        try {
            logger.info("Waiting for Angular load to complete");
            String angularReadyScript = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";
            wait.until(driver -> Boolean.TRUE.equals(((JavascriptExecutor) driver)
                    .executeScript(angularReadyScript)));
        } catch (TimeoutException e) {
            logger.error("Angular load not completed after {} seconds", DEFAULT_TIMEOUT);
            throw e;
        }
    }

    /**
     * Özel bir bekleme koşulu için fluent wait kullanır
     * @param condition Bekleme koşulu
     * @param <T> Dönüş tipi
     * @return Beklenen değer
     */
    public <T> T waitFor(Function<WebDriver, T> condition) {
        try {
            logger.info("Waiting for custom condition");
            return fluentWait.until(condition);
        } catch (TimeoutException e) {
            logger.error("Custom condition not met after {} seconds", DEFAULT_TIMEOUT);
            throw e;
        }
    }
}