package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

import static org.testng.AssertJUnit.assertEquals;

public class TransferMoneyPage extends BasePage{

    private CreateAccountPage createAccountPage;

    public TransferMoneyPage() {
        this.createAccountPage = new CreateAccountPage(); // CreateAccountPage nesnesi oluşturuluyor
    }

    @FindBy(xpath = "//div[contains(text(),'Transfer money') and contains(@class, 'css-146c3p1')]")
    private WebElement transferMoneyButton;

    @FindBy(xpath = "//div[text()='Sender account']")
    private WebElement senderAccountText;

    @FindBy(xpath = "//div[text()='Receiver account']")
    private WebElement receiverAccountText;

    @FindBy(css = "select[style*='height: 48px']")
    private WebElement accountDropdown;

    @FindBy(css = "input.css-11aywtz.r-6taxm2.r-1eh6qqt.r-z2wwpe.r-rs99b7.r-h3s6tt.r-1qhn6m8")
    private WebElement amountTextBox;

    @FindBy(xpath = "//div[contains(@class,'css-175oi2r')]//div[text()='Send']")
    private WebElement sendButton;




    /**
     * Clicks the "Transfer money" button if visible.
     */
    public void clickTransferMoneyButton() {
        try {
            // Elementin görünür olmasını bekle
            wait.until(ExpectedConditions.elementToBeClickable(transferMoneyButton));

            // Elemente tıklama işlemi yap
            transferMoneyButton.click();

            // Log yaz
            logger.info("Clicked on the 'Transfer money' button.");
            System.out.println("Clicked on the 'Transfer money' button.");
        } catch (Exception e) {
            // Element bulunamazsa veya başka bir hata oluşursa log yaz
            logger.error("Failed to click 'Transfer money' button. Reason: " + e.getMessage());
            System.out.println("Transfer money button not found or not clickable.");
        }
    }

    /**
     * Gets the text of the Sender Account element and verifies it.
     */
    public void verifySenderAccountText() {
        try {
            // Elementin görünmesini bekle
            wait.until(ExpectedConditions.visibilityOf(senderAccountText));

            // Elementten text değerini al
            String actualText = senderAccountText.getText();
            String expectedText = "Sender account";

            // Text değerini doğrula
            if (actualText.equals(expectedText)) {
                logger.info("Text is verified: " + actualText);
                System.out.println("Text is verified: " + actualText);
            } else {
                logger.error("Text verification failed. Expected: " + expectedText + ", but found: " + actualText);
                throw new AssertionError("Expected: " + expectedText + ", but found: " + actualText);
            }
        } catch (Exception e) {
            logger.error("An error occurred while verifying the text: " + e.getMessage());
            System.out.println("An error occurred while verifying the text: " + e.getMessage());
        }
    }
    public void verifyReceiverAccountText() {
        try {
            // Elementi bekle ve text al
            wait.until(ExpectedConditions.visibilityOf(receiverAccountText));
            String actualText = receiverAccountText.getText();
            String expectedText = "Receiver account";

            // Text kontrolü
            if (actualText.equals(expectedText)) {
                logger.info("Text verification passed: " + actualText);
                System.out.println("Text verification passed: " + actualText);
            } else {
                logger.error("Text verification failed. Expected: " + expectedText + ", but found: " + actualText);
                throw new AssertionError("Text verification failed. Expected: " + expectedText + ", but found: " + actualText);
            }
        } catch (NoSuchElementException e) {
            logger.error("Receiver account element not found: " + e.getMessage());
            System.out.println("Receiver account element not found: " + e.getMessage());
        }
    }

    public void selectRandomReceiverAccount() {
        // Dropdown elementini locate et
        WebElement dropdown = driver.findElement(By.cssSelector("select"));
        Select selectAccount = new Select(dropdown);

        // Tüm option'ları al ve logla
        List<WebElement> options = selectAccount.getOptions();
        logger.info("Mevcut hesap seçenekleri:");
        for (WebElement option : options) {
            logger.info(option.getText());
        }

        // Random seçim yap
        Random random = new Random();
        int randomIndex = random.nextInt(options.size());

        // Seçilen option'ı al ve seç
        WebElement selectedOption = options.get(randomIndex);
        String selectedText = selectedOption.getText();
        selectAccount.selectByVisibleText(selectedText);

        // Seçilen değeri logla
        logger.info("Random seçilen hesap: " + selectedText);
    }

    /**
     * Clicks on the input field and sends the specified amount.
     *
     * @param amount The amount to be entered in the input box.
     */
    public void enterAmountInTextBox(String amount) {
        try {
            // Input alanına tıkla
            wait.until(ExpectedConditions.elementToBeClickable(amountTextBox));
            amountTextBox.click();
            logger.info("Clicked on the amount text box.");

            // Verilen değeri input alanına gönder
            amountTextBox.clear();
            amountTextBox.sendKeys(amount);
            logger.info("Entered amount: " + amount);
        } catch (NoSuchElementException e) {
            logger.error("Amount text box not found: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while entering the amount: " + e.getMessage());
        }
    }

    /**
     * Clicks the "Send" button if it is available.
     */
    public void clickSendButton() {
        try {
            // Elemanın tıklanabilir olmasını bekle
            wait.until(ExpectedConditions.elementToBeClickable(sendButton));

            // Elemanı tıkla
            sendButton.click();
            logger.info("Clicked on the 'Send' button.");
        } catch (NoSuchElementException e) {
            logger.error("The 'Send' button was not found: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while clicking the 'Send' button: " + e.getMessage());
        }
    }

    public void verifyBalanceAfterTransaction(String transferAmountString) {
        try {
            // 1. Transfer işleminden önce mevcut bakiyeyi al
            double previousBalance = createAccountPage.getCurrentAccountBalance();

            // 2. Transfer miktarını string'den double'a çevir
            double transferAmount = Double.parseDouble(transferAmountString);

            // 3. Transfer işlemini gerçekleştir
           enterAmountInTextBox(transferAmountString);
           clickSendButton();

            // 4. Transfer işleminden sonra yeni bakiyeyi al
            double newBalance = createAccountPage.getCurrentAccountBalance();

            // 5. Eski ve yeni bakiye farkını hesapla
            double balanceDifference = previousBalance - newBalance;

            // 6. Loglama işlemleri
            System.out.println("Previous Balance: " + previousBalance);
            System.out.println("New Balance: " + newBalance);
            System.out.println("Balance Difference: " + balanceDifference);
            System.out.println("Transfer Amount: " + transferAmount);

            // 7. Farkın transfer edilen miktarla aynı olup olmadığını test et
            if (balanceDifference == transferAmount) {
                System.out.println("Test Passed: Balance difference matches the transfer amount.");
            } else {
                System.err.println("Test Failed: Balance difference does NOT match the transfer amount.");
            }

            // Assertion
            assertEquals("Balance difference does not match transfer amount!", transferAmount, balanceDifference, 0.01);

        } catch (Exception e) {
            System.err.println("Error during balance verification: " + e.getMessage());
            throw e;
        }
    }

}
