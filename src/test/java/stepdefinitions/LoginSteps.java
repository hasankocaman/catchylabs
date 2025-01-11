package stepdefinitions;

import config.ConfigurationManager;
import driver.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

public class LoginSteps extends BaseSteps {
    private final WebDriver driver = DriverManager.getDriver();
    private final LoginPage loginPage = new LoginPage();
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);
    private final Hooks hooks = new Hooks();

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        logger.info("Navigating to login page");
        hooks.navigateToUrl(ConfigurationManager.getInstance().getProperty("baseUrl"));
    }

    @When("I enter username and password")
    public void i_enter_username_and_password() {
        logger.info("Entering credentials");
        loginPage.login();
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        logger.info("Clicking login button");
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        logger.info("Verifying successful login");
        System.out.println("Verifying successful login");
        Assert.assertTrue("Login was not successful",
                loginPage.isLoginSuccessful());
    }

    @When("I enter invalid username and password")
    public void i_enter_invalid_username_and_password() {
        String invalidUsername = LoginPage.generateInvalidUsername();
        System.out.println("invalidUsername = " + invalidUsername);
        String invalidPassword = LoginPage.generateInvalidPassword();
        System.out.println("invalidPassword = " + invalidPassword);

        loginPage.performLogin(invalidUsername, invalidPassword);

        System.out.println("Invalid Username: " + invalidUsername);
        System.out.println("Invalid Password: " + invalidPassword);
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedErrorMessage) {
        System.out.println("simdi iShouldSeeAnErrorMessage metodundasin");
        String actualErrorMessage = loginPage.getErrorMessage();
        Assert.assertEquals("Error message does not match!", expectedErrorMessage, actualErrorMessage);
    }
}