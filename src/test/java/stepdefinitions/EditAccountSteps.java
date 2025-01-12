package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.CreateAccountPage;
import pages.EditAccountPage;
import pages.TransferMoneyPage;

public class EditAccountSteps extends BaseSteps {



    private final EditAccountPage editAccountPage = new EditAccountPage();
    CreateAccountPage createAccountPage=new CreateAccountPage();
    private String eskiMetin;
    private String yeniMetin;


    @When("I click edit account buton")
    public void iClickEditAccountButon() {
        editAccountPage.clickEditAccount();
    }

    @And("I click update buton and edit the account name")
    public void iClickUpdateButonAndEditTheAccountName() throws InterruptedException {
        // Eski metni sakla
        eskiMetin = editAccountPage.getInputText();
        System.out.println("Mevcut metin: " + eskiMetin);

        // Yeni metni oluştur ve sakla
        yeniMetin = "Yeni mevduat hesabım " + EditAccountPage.generateRandomString();
        editAccountPage.clearAndWriteText(yeniMetin);
        editAccountPage.clickUpdateButton();

        // CreateAccountPage doğrulaması
        createAccountPage.assertAccountNameContains(yeniMetin);
    }

    @Then("verify that the user could update the account name")
    public void verifyThatTheUserCouldUpdateTheAccountName() throws InterruptedException {
        createAccountPage.assertAccountNameContains(yeniMetin);
    }
}