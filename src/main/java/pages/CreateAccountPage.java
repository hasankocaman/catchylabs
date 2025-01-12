package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static org.testng.Assert.assertTrue;


import java.util.List;



/**
 * This class represents the "Create Account" page and provides methods to interact with
 * its elements such as verifying text, interacting with dropdowns, and validating options.
 */
public class CreateAccountPage extends BasePage {

    @FindBy(xpath = "//div[text()='Create account']")
    private WebElement createAccountText;

    @FindBy(xpath = "//div[text()='Open Money Transfer']")
    private WebElement openMoneyTransferButton;

    @FindBy(xpath = "//div[@tabindex='0']/div[contains(text(),'Create an account')]")
    private WebElement createAccountButton;

    @FindBy(xpath = "//select")
    public WebElement dropdown;

    @FindBy(css = "input[class*='css-11aywtz'][spellcheck='true']")
    private WebElement accountNameTextBox;

    @FindBy(xpath = "//div[text()='Create']")
    private WebElement createButton;


    // Locator for "mevduat hesabim" elements
    @FindBy(css = "div.css-146c3p1.r-1ozpqpt.r-yv33h5.r-1b43r93")
    private List<WebElement> accountNames;

    // Locator for date elements
    @FindBy(css = "div.css-146c3p1.r-1ozpqpt.r-yv33h5.r-1b43r93")
    private List<WebElement> accountDates;

    // Locator for balance elements
    @FindBy(css = "div.css-146c3p1.r-1ozpqpt.r-yv33h5.r-1b43r93[style='margin-right: 4px;']")
    private WebElement accountBalance;

    /**
     * Finds and returns a specific account WebElement by its text.
     * If the element is not found, logs an error and continues execution.
     *
     * @param expectedText The text of the account to find.
     * @return WebElement if found, null otherwise.
     */
    public WebElement getSpecificAccountName(String expectedText) {
        try {
            // Tüm account öğelerinin görünmesini bekle
            wait.until(ExpectedConditions.visibilityOfAllElements(accountNames));

            // Account isimlerini döngü ile kontrol et
            for (WebElement account : accountNames) {
                if (account.getText().equals(expectedText)) {
                    logger.info("Found account with text: " + expectedText);
                    System.out.println("Found account with text: " + expectedText);
                    return account;
                }
            }
            // Eğer hiçbir eşleşme bulunmazsa
            logger.warn("No account found with text: " + expectedText);
            System.out.println("No account found with text: " + expectedText);
        } catch (Exception e) {
            // Element bulunamazsa hata mesajı yaz ve devam et
            logger.error("An error occurred while finding the account: " + e.getMessage());
            System.out.println("An error occurred while finding the account: " + e.getMessage());
        }
        // Eğer account bulunamazsa null döner
        return null;
    }


    /**
     * Gets the latest date from the account dates.
     *
     * @return String of the latest date.
     */
    public String getLatestDate() {
        wait.until(ExpectedConditions.visibilityOfAllElements(accountDates));
        String latestDate = accountDates.get(accountDates.size() - 1).getText();
        System.out.println("Latest Date: " + latestDate);
        return latestDate;
    }

    /**
     * Gets the current balance of a specific account as a double.
     *
     * @return double representing the current balance of the account.
     */
    public double getCurrentAccountBalance() {
        try {
            // Elementin görünür olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(accountBalance));

            // Text içeriğini al
            String balanceText = accountBalance.getText();

            // Log işlemi
            System.out.println("Balance Text Retrieved: " + balanceText);

            // Virgülleri kaldır, string'i double'a çevir
            double currentBalance = Double.parseDouble(balanceText.replace(",", "").trim());

            // Log ile sonucu göster
            System.out.println("Current Balance as Double: " + currentBalance);

            return currentBalance;
        } catch (Exception e) {
            System.err.println("Error while retrieving account balance: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Asserts if the account name contains the expected text.
     *
     * @param expectedText The expected text to check.
     */
    /**
     * Asserts if the account name contains the expected text.
     *
     * @param expectedText The expected text to check.
     */
    public void assertAccountNameContains(String expectedText) {
        try {
            // WebElement'i alın
            WebElement account = getSpecificAccountName(expectedText);

            // Eğer element bulunamazsa exception atar
            if (account == null) {
                throw new NoSuchElementException("No account found with text: " + expectedText);
            }

            // WebElement'in text'ini alın
            String actualText = account.getText();
            System.out.println("Actual Text Retrieved: " + actualText);

            // Eğer text null veya boşsa hata mesajı
            if (actualText == null || actualText.isEmpty()) {
                throw new AssertionError("Actual text is null or empty!");
            }

            // Assert işlemi
            assertTrue(actualText.contains(expectedText), "Account name does not contain '" + expectedText + "'");
            System.out.println("Assertion Passed: Account name contains '" + expectedText + "'");
        } catch (NoSuchElementException e) {
            System.err.println("No such element found with text: " + expectedText);
            throw e; // Testin raporlanması için exception fırlatılır
        } catch (AssertionError e) {
            System.err.println("Assertion failed: " + e.getMessage());
            throw e; // Assertion hataları raporlanır
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw e; // Diğer hatalar raporlanır
        }
    }



    /**
     * Verifies if the "Create account" text is visible and matches the expected text.
     * If the element is not found, logs a message and continues execution.
     */
    public void createAccountTextVerify() {
        try {
            // Elementin görünür olmasını bekle
            wait.until(ExpectedConditions.visibilityOf(createAccountText));

            // Elementin text'ini al
            String actualText = createAccountText.getText();
            String expectedText = "Create account";

            // Text'i kontrol et
            if (!actualText.equals(expectedText)) {
                throw new AssertionError("Expected text: " + expectedText + ", but found: " + actualText);
            }

            // Başarılı durum
            System.out.println("Create account text is visible and matches the expected text: " + actualText);
        } catch (Exception e) {
            // Element bulunamazsa veya başka bir hata oluşursa loglama yap
            System.err.println("Create account text is not found. Continuing execution.");
        }

        // Kodun devam etmesi için mesaj
        System.out.println("Continuing with the next steps...");
    }


    public boolean isDropdownPopulated() {
        Select select = new Select(dropdown);
        return !select.getOptions().isEmpty();
    }

    public boolean isOptionPresent(String expectedValue) {
        Select select = new Select(dropdown);
        for (WebElement option : select.getOptions()) {
            if (option.getAttribute("value").equals(expectedValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean areBothOptionsPresent() {
        Select select = new Select(dropdown);
        boolean checkingExists = false;
        boolean savingExists = false;
        for (WebElement option : select.getOptions()) {
            String value = option.getAttribute("value");
            if ("CHECKING".equals(value)) {
                checkingExists = true;
            }
            if ("SAVING".equals(value)) {
                savingExists = true;
            }
            if (checkingExists && savingExists) {
                return true;
            }
        }
        return false;
    }

    public void openMoneyTransferClick() {
        openMoneyTransferButton.click();
    }


    /**
     * Clicks on the "Create Account" button if available.
     * If the button is not found, logs "Account is already created" and continues execution.
     */
    public void clickCreateAccountButton() {
        try {
            // Elementin tıklanabilir olduğunu bekle
            wait.until(ExpectedConditions.elementToBeClickable(createAccountButton));

            // Click işlemini gerçekleştir
            createAccountButton.click();
            System.out.println("Create Account button clicked successfully.");
        } catch (Exception e) {
            // Eğer element bulunamazsa log yazdır
            System.out.println("Account is already created. Skipping create account step.");
        }

        // Kodun devam etmesini sağlamak için burada başka işlemler yapabilirsiniz
        System.out.println("Continuing with the next steps...");
    }


    public void selectAccountTypeFromDropdown(String accountType) {
        try {
            // Dropdown görünene kadar bekle
            wait.until(ExpectedConditions.visibilityOf(dropdown));

            // Dropdown menüsünü seç
            Select dropdown = new Select(this.dropdown);

            // Seçilen hesap türüne göre dropdown menüsünden seçim yap
            if ("SAVING".equalsIgnoreCase(accountType)) {
                dropdown.selectByValue("SAVING");
                System.out.println("Selected account type: SAVING");
            } else if ("CHECKING".equalsIgnoreCase(accountType)) {
                dropdown.selectByValue("CHECKING");
                System.out.println("Selected account type: CHECKING");
            } else {
                throw new IllegalArgumentException("Invalid account type: " + accountType);
            }
        } catch (NoSuchElementException e) {
            // Eğer dropdown bulunamazsa log mesajı yaz ve devam et
            System.err.println("Select element not found because the account type is already selected.");
        } catch (Exception e) {
            // Diğer hataları yakala ve log mesajı yaz
            System.err.println("An error occurred while selecting account type: " + e.getMessage());
        }
    }


    /**
     * Clicks on the account name text box and inputs the given string.
     *
     * @param accountName The string value to be entered into the text box.
     */
    /**
     * Clicks on the account name text box and inputs the given string, with logging for each step.
     *
     * @param accountName The string value to be entered into the text box.
     */
    public void enterAccountName(String accountName) {
        // Wait until the element is visible and interactable
        wait.until(ExpectedConditions.visibilityOf(accountNameTextBox));
        wait.until(ExpectedConditions.elementToBeClickable(accountNameTextBox));

        // Log the action for clicking the text box
        System.out.println("Attempting to click the account name text box.");

        // Click the text box to activate it
        accountNameTextBox.click();
        System.out.println("Account name text box clicked successfully.");

        // Clear the existing text in the text box
        accountNameTextBox.clear();
        System.out.println("Existing text in the text box cleared.");

        // Log the action for entering the text
        System.out.println("Sending text to the account name text box: " + accountName);

        // Send the user-provided string to the text box
        accountNameTextBox.sendKeys(accountName);
        System.out.println("Text '" + accountName + "' entered successfully into the account name text box.");
    }
    /**
     * Clicks on the "Create" button and logs the action.
     */
    public void clickCreateButton() {
        // Wait until the element is visible and clickable
        wait.until(ExpectedConditions.visibilityOf(createButton));
        wait.until(ExpectedConditions.elementToBeClickable(createButton));

        // Log before clicking the button
        System.out.println("Attempting to click the 'Create' button.");

        // Perform the click action
        createButton.click();

        // Log after the button is clicked
        System.out.println("'Create' button clicked successfully.");
    }


}