package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.security.SecureRandom;
import java.time.Duration;

public class EditAccountPage extends BasePage {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();
    CreateAccountPage createAccountPage=new CreateAccountPage();

    @FindBy(xpath = "(//div[contains(@class, 'css-175oi2r') and .//div[text()='Edit account']])[9]")
    private WebElement editAccountButton;

    @FindBy(xpath = "//div[@tabindex='0' and @class='css-175oi2r r-1i6wzkk r-lrvibr r-1loqt21 r-1otgn73 r-1awozwy r-169ebfh r-z2wwpe r-h3s6tt r-1777fci r-tsynxw r-13qz1uu']//div[text()='UPDATE']")
    private WebElement updateButton;

//    @FindBy(css = "input.css-11aywtz.r-6taxm2.r-1eh6qqt.r-z2wwpe.r-rs99b7.r-h3s6tt.r-1qhn6m8")
//    private WebElement textInput;

    @FindBy(css = "input[class*='css-11aywtz']")
    private WebElement textInput;

    private WebDriverWait wait;
    private JavascriptExecutor js;

    public EditAccountPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }

    public void clickEditAccount() {
        try {
            // Elementin görünür ve tıklanabilir olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(editAccountButton));
            wait.until(ExpectedConditions.elementToBeClickable(editAccountButton));

            try {
                // Normal tıklama dene
                editAccountButton.click();
            } catch (ElementClickInterceptedException e) {
                // JavaScript ile tıklama dene
                js.executeScript("arguments[0].scrollIntoView(true);", editAccountButton);
                js.executeScript("arguments[0].click();", editAccountButton);
            }

            // Tıklama sonrası input alanının görünür olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(textInput));

        } catch (Exception e) {
            System.err.println("Edit Account butonuna tıklarken hata: " + e.getMessage());
            throw e;
        }
    }

    public void clickUpdateButton() throws InterruptedException {
        try {
            wait.until(ExpectedConditions.visibilityOf(updateButton));
            wait.until(ExpectedConditions.elementToBeClickable(updateButton));

            try {
                updateButton.click();
            } catch (ElementClickInterceptedException e) {
                js.executeScript("arguments[0].click();", updateButton);
            }

            // Update sonrası bekleme süresini artıralım
            Thread.sleep(3000);

        } catch (Exception e) {
            System.err.println("UPDATE butonu hatası: " + e.getMessage());
            throw e;
        }
    }

    public String getInputText() {
        try {
            // Input alanının görünür olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(textInput));

            // Değeri JavaScript ile al (daha güvenilir)
            String value = (String) js.executeScript("return arguments[0].value;", textInput);
            System.out.println("Alınan metin: " + value);
            return value;

        } catch (Exception e) {
            System.err.println("Input alanından text alınırken hata: " + e.getMessage());
            throw e;
        }
    }

    public void clearAndWriteText(String newText) throws InterruptedException {
        try {
            // Input alanının görünür ve tıklanabilir olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(textInput));
            wait.until(ExpectedConditions.elementToBeClickable(textInput));

            // JavaScript ile input alanını temizle
            js.executeScript("arguments[0].value = '';", textInput);

            // Karakterleri tek tek gönder
            for (char c : newText.toCharArray()) {
                textInput.sendKeys(String.valueOf(c));
                Thread.sleep(50); // Her karakter arasında küçük bir bekleme
            }

            // Değerin doğru yazıldığını kontrol et
            wait.until(driver -> textInput.getAttribute("value").equals(newText));
            System.out.println("Yeni metin başarıyla yazıldı: " + newText);

        } catch (Exception e) {
            System.err.println("Metin yazılırken hata: " + e.getMessage());
            throw e;
        }
    }

    public boolean areTextsDifferentDynamically() throws InterruptedException {
        try {
            String oldText = getInputText();
            System.out.println("Eski metin: " + oldText);

            String newText = "Yeni mevduat hesabım " + generateRandomString();
            clearAndWriteText(newText);

            String updatedText = getInputText();
            System.out.println("Güncellenmiş metin: " + updatedText);

            boolean result = oldText != null && !oldText.equals(updatedText);
            System.out.println(result ? "Metinler farklı - Test başarılı!" : "Metinler aynı - Test başarısız!");

            return result;

        } catch (Exception e) {
            System.err.println("Metin karşılaştırma hatası: " + e.getMessage());
            throw e;
        }
    }

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}