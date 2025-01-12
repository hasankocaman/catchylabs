package stepdefinitions;

import config.ConfigurationManager;
import driver.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.CreateAccountPage;
import pages.LoginPage;
import pages.TransferMoneyPage;

public class TransferMoneySteps extends BaseSteps {

    // WebDriver örneğini DriverManager'dan alır.
    private final WebDriver driver = DriverManager.getDriver();

    private final TransferMoneyPage transferMoneyPage = new TransferMoneyPage();
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);

    // CreateAccountPage sınıfından bir örnek oluşturur.
    CreateAccountPage createAccountPage = new CreateAccountPage();



    @When("I click Transfer Money buton")
    public void i_click_transfer_money_buton() {
        logger.info("I click Transfer Money buton metodundasın");
        transferMoneyPage.clickTransferMoneyButton();
    }
    
    @Then("I see Sender account and Receiver Account texts")
    public void i_see_sender_account_and_receiver_account_texts() {
        transferMoneyPage.verifySenderAccountText();
        transferMoneyPage.verifyReceiverAccountText();
    }


    @And("I select a random receiver account")
    public void iSelectARandomReceiverAccount() {
        transferMoneyPage.selectRandomReceiverAccount();
    }

    @And("I put data {string} amount text box")
    public void iPutDataAmountTextBox(String amount) {
        transferMoneyPage.enterAmountInTextBox(amount);
    }

    @Then("click send button and check amount")
    public void clickSendButtonAndCheckAmount() {
        transferMoneyPage.clickSendButton();
        createAccountPage.getCurrentAccountBalance();
        transferMoneyPage.verifyBalanceAfterTransaction("100");
    }
}