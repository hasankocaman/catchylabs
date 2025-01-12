package pages;

import org.openqa.selenium.*;
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

    private final By cardNumberLocator = By.xpath("//input[@autocapitalize='sentences' and @autocomplete='on']");
    private final By cardHolderLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[2]");
    private final By expiryDateLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[3]");
    private final By cvvLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[4]");
    private final By amountLocator = By.xpath("(//input[@autocapitalize='sentences' and @autocomplete='on'])[5]");

    public void clickAddMoneyButton() throws InterruptedException {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addMoneyButton)).click();
            Thread.sleep(2000);
            System.out.println("Para Ekle butonuna başarıyla tıklandı.");
        } catch (Exception e) {
            System.err.println("Para Ekle butonuna tıklarken hata: " + e.getMessage());
            throw e;
        }
    }

    private void enterInput(String text, By locator, String fieldName) throws InterruptedException {
        try {
            Thread.sleep(2000);

            WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(inputField));

            // JavaScript ile tıkla ve yaz
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", inputField);
            Thread.sleep(500);
            js.executeScript("arguments[0].value = arguments[1];", inputField, text);

            System.out.println(fieldName + " başarıyla girildi: " + text);
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println(fieldName + " alanına veri girilirken hata: " + e.getMessage());
            throw e;
        }
    }

    public void enterCardInformation(String cardNumber, String cardHolder, String expiryDate, String cvv) throws InterruptedException {
        Thread.sleep(2000);
        enterInput(cardNumber, cardNumberLocator, "Kart numarası");
        enterInput(cardHolder, cardHolderLocator, "Kart sahibi adı");
        enterInput(expiryDate, expiryDateLocator, "Son kullanma tarihi");
        enterInput(cvv, cvvLocator, "CVV");
    }

    public void enterAmount(String amount) throws InterruptedException {
        enterInput(amount, amountLocator, "Tutar");
    }
}