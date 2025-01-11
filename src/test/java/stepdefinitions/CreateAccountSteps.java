package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.CreateAccountPage;

public class CreateAccountSteps {

    // WebDriver örneğini DriverManager'dan alır.
    private final WebDriver driver = DriverManager.getDriver();

    // CreateAccountPage sınıfından bir örnek oluşturur.
    CreateAccountPage createAccountPage = new CreateAccountPage();

    // Logger, loglama işlemleri için kullanılır.
    private static final Logger logger = LogManager.getLogger(CreateAccountSteps.class);

    /**
     * Kullanıcı "Create Account" sayfasına eriştiğinde, başlık metni doğrulanır.
     */
    @Given("I am on the account creation page")
    public void i_am_on_the_account_creation_page() {
        logger.info("Verifying account creation page");
        System.out.println("Verifying account creation page");

        // Create Account metninin doğru şekilde göründüğünü doğrular.
        createAccountPage.createAccountTextVerify();
    }

    @And("the dropdown menu contains the {string} and {string} account types")
    public void theDropdownMenuContainsTheAndAccountTypes(String savingAccount, String checkingAccount) {
        logger.info("Verifying dropdown menu options");
        System.out.println("Checking dropdown menu options");

        // Dropdown menüdeki seçeneklerin doğrulanması
        assert createAccountPage.isOptionPresent("SAVING");
        assert createAccountPage.isOptionPresent("CHECKING");
    }

    @When("I select the {string} account type")
    public void iSelectTheAccountType(String accountType) {
        logger.info("Selecting account type: " + accountType);
        System.out.println("Selecting account type: " + accountType);

        // Hesap türünün seçilmesi
        createAccountPage.selectAccountTypeFromDropdown(accountType);
    }

    @When("I enter an account name")
    public void iEnterAnAccountName() {
        logger.info("Entering account name");
        System.out.println("Entering account name");
createAccountPage.enterAccountName("mevduat hesabim");
createAccountPage.clickCreateButton();

    }

    @Then("the account should be created successfully")
    public void the_account_should_be_created_successfully() {
        logger.info("Verifying account creation success");
        System.out.println("Account creation verified successfully.");
        createAccountPage.getSpecificAccountName("mevduat hesabim");
        createAccountPage.getLatestDate();
        createAccountPage.getHighestBalance();
        createAccountPage.assertAccountNameContains("mevduat hesabim");
    }

    @Then("the account balance should be {int}")
    public void the_account_balance_should_be(Integer expectedBalance) {
        logger.info("Verifying account balance: " + expectedBalance);
        System.out.println("Checking account balance: " + expectedBalance);

        // Burada, hesap bakiyesi doğrulama kodları yazılır (örneğin, bir API çağrısı veya UI doğrulaması).
    }

    @Then("the account should have no specified currency")
    public void the_account_should_have_no_specified_currency() {
        logger.info("Verifying account has no specified currency");
        System.out.println("Checking that account has no specified currency.");

        // Burada, hesap para biriminin belirtilmediğini doğrulayan kodlar yazılır.
    }

    @Then("user clicks open money transfer buton")
    public void user_clicks_open_money_transfer_buton() {
        createAccountPage.openMoneyTransferClick();
    }

    @And("user clicks create an account buton")
    public void userClicksCreateAnAccountButon() {
        createAccountPage.createAccountClick();
    }

    public void selectAccountTypeFromDropdown(String accountType) {
        // Dropdown menüsünü seç
        Select dropdown = new Select(createAccountPage.dropdown);

        // Seçilen hesap türüne göre dropdown menüsünden seçim yap
        if ("SAVING".equalsIgnoreCase(accountType)) {
            dropdown.selectByValue("SAVING");
        } else if ("CHECKING".equalsIgnoreCase(accountType)) {
            dropdown.selectByValue("CHECKING");
        } else {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
}