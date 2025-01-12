package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.AddMoneyPage;
import pages.CreateAccountPage;
import pages.TransferMoneyPage;

public class AddMoneySteps extends BaseSteps {

    // WebDriver örneğini DriverManager'dan alır.
    private final WebDriver driver = DriverManager.getDriver();

    private final AddMoneyPage addMoneyPage = new AddMoneyPage();
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);

    // CreateAccountPage sınıfından bir örnek oluşturur.
    CreateAccountPage createAccountPage = new CreateAccountPage();


    @When("I click add money buton")
    public void iClickAddMoneyButon() throws InterruptedException {
        addMoneyPage.clickAddMoneyButton();
    }


//    @And("I put datas to required fields and add amount {string} and click add buton")
//    public void iPutDatasToRequiredFieldsAndAddAmountAndClickAddButon(String amount) throws Exception {
//
//
//    }

    @And("I put datas to required fields and add amount {string}")
    public void iPutDatasToRequiredFieldsAndAddAmount(String amount) throws InterruptedException {
        addMoneyPage.enterCardInformation(
                "1234 1234 1234 1234",  // Kart numarası
                "Hasan Kocaman",        // Kart sahibi
                "10/26",                 // Son kullanma tarihi
                "110"                   // CVV
        );
        addMoneyPage.enterAmount(amount);
        System.out.println("Amount entered: " + amount);
        logger.info("Amount entered: " + amount);
    }

    @And("click add buton")
    public void clickAddButon() throws Exception {
        addMoneyPage.clickAddButton();
    }


}