package stepdefinitions;

import config.ConfigurationManager;
import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

import static org.junit.Assert.assertEquals;
import static pages.LoginPage.generateInvalidPassword;
import static pages.LoginPage.generateInvalidUsername;

public class LoginSteps {
    private final WebDriver driver = DriverManager.getDriver();
    private final LoginPage loginPage = new LoginPage();
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);

    @Before
    public void setup(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png",
                    "Screenshot-" + scenario.getName());
        }
        DriverManager.quitDriver();
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        logger.info("Navigating to login page");
        driver.get(ConfigurationManager.getInstance().getProperty("baseUrl"));
    }

    @When("I enter username and password")
    public void i_enter_username_and_password() {
        logger.info("Entering credentials");
        loginPage.login();
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        logger.info("Clicking login button");
        System.out.println("Clicking login button");
        loginPage.clickLoginButton();
        System.out.println(" login button clicked");
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        logger.info("Verifying successful login");
        System.out.println("Verifying successful login");
        Assert.assertTrue("Login was not successful",
                loginPage.isLoginSuccessful());
    }

    protected void waitForPageLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.info("Waited {} seconds for page load", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Wait interrupted", e);
        }
    }


    @When("I enter invalid username and password")
    public void i_enter_invalid_username_and_password() {
        String invalidUsername = generateInvalidUsername();
        System.out.println("invalidUsername = " + invalidUsername);
        String invalidPassword = generateInvalidPassword();
        System.out.println("invalidPassword = " + invalidPassword);

        loginPage.performLogin(invalidUsername, invalidPassword);

        System.out.println("Invalid Username: " + invalidUsername);
        System.out.println("Invalid Password: " + invalidPassword);
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedErrorMessage) {
        System.out.println("simdi iShouldSeeAnErrorMessage metodundasin");
        String actualErrorMessage = loginPage.getErrorMessage();
        assertEquals("Error message does not match!", expectedErrorMessage, actualErrorMessage);
    }

}