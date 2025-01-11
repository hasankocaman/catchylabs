package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static org.testng.Assert.assertTrue;


import java.util.List;

import static org.testng.Assert.assertTrue;


/**
 * This class represents the "Create Account" page and provides methods to interact with
 * its elements such as verifying text, interacting with dropdowns, and validating options.
 */
public class CreateAccountPage extends BasePage {

    // WebElement'ler
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
    @FindBy(css = "div.css-146c3p1.r-1ozpqpt.r-yv33h5.r-1b43r93")
    private List<WebElement> accountBalances;

    /**
     * Gets the WebElement with the specified account name text.
     *
     * @param expectedText The expected text of the account name.
     * @return WebElement of the matching account name.
     */
    public WebElement getSpecificAccountName(String expectedText) {
        wait.until(ExpectedConditions.visibilityOfAllElements(accountNames));
        for (WebElement account : accountNames) {
            if (account.getText().equals(expectedText)) {
                System.out.println("Found account with text: " + expectedText);
                return account;
            }
        }
        throw new NoSuchElementException("No account found with text: " + expectedText);
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
     * Gets the highest balance from the list of account balances.
     *
     * @return double of the highest balance.
     */
    public double getHighestBalance() {
        wait.until(ExpectedConditions.visibilityOfAllElements(accountBalances));
        double highestBalance = 0.0;
        for (WebElement balance : accountBalances) {
            String text = balance.getText().replace(",", "").trim();
            double currentBalance = Double.parseDouble(text);
            if (currentBalance > highestBalance) {
                highestBalance = currentBalance;
            }
        }
        System.out.println("Highest Balance: " + highestBalance);
        return highestBalance;
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


    // Metotlar
    public void createAccountTextVerify() {
        wait.until(ExpectedConditions.visibilityOf(createAccountText));
        String actualText = createAccountText.getText();
        String expectedText = "Create account";
        if (!actualText.equals(expectedText)) {
            throw new AssertionError("Expected text: " + expectedText + ", but found: " + actualText);
        }
        System.out.println("Create account text is visible and matches the expected text: " + actualText);
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

    public void createAccountClick() {
        createAccountButton.click();
    }

    public void selectAccountTypeFromDropdown(String accountType) {
        // Dropdown menüsünü seç
        Select dropdown = new Select(this.dropdown);

        // Seçilen hesap türüne göre dropdown menüsünden seçim yap
        if ("SAVING".equalsIgnoreCase(accountType)) {
            dropdown.selectByValue("SAVING");
        } else if ("CHECKING".equalsIgnoreCase(accountType)) {
            dropdown.selectByValue("CHECKING");
        } else {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
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