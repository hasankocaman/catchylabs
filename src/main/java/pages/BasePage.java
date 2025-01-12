package pages;

import driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.ElementClickInterceptedException;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final Logger logger = LogManager.getLogger(this.getClass());

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected void waitForElementVisible(WebElement element) {
        logger.debug("Element görünür olana kadar bekleniyor: {}", element);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element görünür hale gelmedi: {}", element);
            throw new RuntimeException("Element görünür değil", e);
        }
    }

    protected void waitForElementClickable(WebElement element) {
        logger.debug("Element tıklanabilir olana kadar bekleniyor: {}", element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element tıklanabilir hale gelmedi: {}", element);
            throw new RuntimeException("Element tıklanabilir değil", e);
        }
    }

    protected void click(WebElement element) {
        try {
            waitForElementVisible(element);
            waitForElementClickable(element);
            element.click();
            logger.info("Elemente başarıyla tıklandı: {}", element);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Normal tıklama başarısız oldu, JavaScript ile tıklama deneniyor: {}", element);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            logger.info("JavaScript ile tıklama başarılı: {}", element);
        } catch (Exception e) {
            logger.error("Elemente tıklarken hata oluştu: {}", element, e);
            throw new RuntimeException("Element tıklanamadı", e);
        }
    }

    protected void sendKeys(WebElement element, String text) {
        try {
            waitForElementVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Text başarıyla girildi: '{}', element: {}", text, element);
        } catch (Exception e) {
            logger.error("Text girilirken hata oluştu: '{}', element: {}", text, element, e);
            throw new RuntimeException("Text girilemedi", e);
        }
    }

    protected void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Elemente scroll yapıldı: {}", element);
        } catch (Exception e) {
            logger.error("Elemente scroll yapılırken hata oluştu: {}", element, e);
            throw new RuntimeException("Elemente scroll yapılamadı", e);
        }
    }


}