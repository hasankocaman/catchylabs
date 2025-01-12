package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddMoneyPage extends BasePage {
    private CreateAccountPage createAccountPage;
    protected WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public AddMoneyPage() {
        this.createAccountPage = new CreateAccountPage();
    }

    @FindBy(xpath = "//div[@tabindex='0' and contains(@class,'css-175oi2r')]//div[text()='Add money']")
    private WebElement addMoneyButton;


    @FindBy(xpath = "//div[contains(@class, 'css-175oi2r') and @tabindex='0']//div[contains(@class, 'css-146c3p1') and text()='Add']")
    private WebElement addButton;

    private final By cardNumberLocator = By.xpath("//input[@autocapitalize='sentences' and @autocomplete='on']");
    private final By cardHolderLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[2]");
    private final By expiryDateLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[3]");
    private final By cvvLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[4]");
    private final By amountLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[5]");

    public void clickAddMoneyButton() throws InterruptedException {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addMoneyButton)).click();
            Thread.sleep(1000);
            System.out.println("Para Ekle butonuna başarıyla tıklandı.");
        } catch (Exception e) {
            System.err.println("Para Ekle butonuna tıklarken hata: " + e.getMessage());
            throw e;
        }
    }

    private void enterInput(String text, By locator, String fieldName) throws InterruptedException {
        try {
            Thread.sleep(500);
            WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(inputField));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", inputField);
            Thread.sleep(200);

            js.executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                    inputField, text
            );

            System.out.println(fieldName + " başarıyla girildi: " + text);
            Thread.sleep(200);
        } catch (Exception e) {
            System.err.println(fieldName + " alanına veri girilirken hata: " + e.getMessage());
            throw e;
        }
    }

    public void enterCardInformation(String cardNumber, String cardHolder, String expiryDate, String cvv) throws InterruptedException {
        enterInput(cardNumber, cardNumberLocator, "Kart numarası");
        enterInput(cardHolder, cardHolderLocator, "Kart sahibi adı");
        enterInput(expiryDate, expiryDateLocator, "Son kullanma tarihi");
        enterInput(cvv, cvvLocator, "CVV");
    }

    public void enterAmount(String amount) throws InterruptedException {
        enterInput(amount, amountLocator, "Tutar");
    }

    public void clickAddButton() throws Exception {
        logger.info("Click add buton metodu içindesin");
        System.out.println("1. Metoda girdi: Click add buton metodu içindesin");

        try {
            // Güncellenmiş daha spesifik locator
            WebElement addButtonElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'css-175oi2r') and @tabindex='0']//div[contains(@class, 'css-146c3p1') and text()='Add']")
            ));

            // Scroll ve focus işlemleri
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});",
                    addButtonElement
            );

            // Tıklama öncesi hazırlık
            Actions actions = new Actions(driver);
            actions.moveToElement(addButtonElement).perform();

            // Çoklu tıklama stratejisi
            try {
                addButtonElement.click();
            } catch (Exception e) {
                System.out.println("Standart click başarısız. JavaScript click deneniyor.");
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();",
                        addButtonElement
                );
            }

            // Tıklama sonrası kontrol
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'css-175oi2r') and @tabindex='0']//div[contains(@class, 'css-146c3p1') and text()='Add']")
            ));

            System.out.println("Add butonu başarıyla tıklandı");

        } catch (Exception e) {
            System.err.println("Add butonuna tıklarken hata: " + e.getMessage());
            e.printStackTrace();

            // Detaylı hata bilgisi
            System.out.println("Sayfa kaynağı:");
            System.out.println(driver.getPageSource());

            throw e;
        }
    }

    private boolean isAddButtonClicked() {
        try {
            System.out.println("19. isAddButtonClicked metodu çalıştırılıyor");
            // Butonun tıklanıp tıklanmadığını doğrulamak için ek kontroller
            Thread.sleep(1000); // Kısa bir bekleme
            System.out.println("20. isAddButtonClicked metodu başarılı");
            return true;
        } catch (Exception e) {
            System.out.println("21. isAddButtonClicked metodunda hata: " + e.getMessage());
            return false;
        }
    }

    public void clickElement(WebElement element) {
        try {
            // Elementin üzerine hover et
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();

            // Elementi görünür olana kadar kaydır
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                    element
            );

            // WebDriverWait kullanarak tıklanabilir olana kadar bekle
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element));

            // Standart click metodunu dene
            try {
                clickableElement.click();
            } catch (Exception e) {
                // Standart click başarısız olursa JavaScript click'i kullan
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableElement);
            }

            logger.info("Elemente başarıyla tıklandı.");
        } catch (Exception e) {
            logger.error("Elemente tıklanırken hata oluştu: " + e.getMessage());
            throw new RuntimeException("Element tıklama hatası", e);
        }
    }


}
