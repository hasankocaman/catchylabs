package pages;

import config.Configuration;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login sayfası için Page Object class'ı.
 * Sayfa elementlerini ve login işlemlerini yönetir.
 */
public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

       @FindBy(xpath = "//div[text()='Login']")
    private WebElement loginButton;


    @FindBy(xpath = "//span[text()='Challenge name:']")
    private WebElement challengeNameLabel;


    @FindBy(css = "div.css-146c3p1.r-howw7u.r-1b43r93")
    private WebElement errorMessage;


    /**
     * Config dosyasından kullanıcı bilgileri ile login olur
     */
    public void login() {
        String username = Configuration.getInstance().getProperty("test.username");
        String password = Configuration.getInstance().getProperty("test.password");

        logger.info("Attempting to login with username from config: {}", username);
        performLogin(username, password);
    }

    /**
     * Verilen kullanıcı bilgileri ile login olur
     * @param username kullanıcı adı
     * @param password şifre
     */
    public void login(String username, String password) {
        logger.info("Attempting to login with provided credentials. Username: {}", username);
        performLogin(username, password);
    }

    /**
     * Login işlemini gerçekleştirir
     * @param username kullanıcı adı
     * @param password şifre
     */
    private void performLogin(String username, String password) {
        try {
            logger.debug("Clearing and entering username");
            waitForElementVisible(usernameInput);
            sendKeys(usernameInput, username);

            logger.debug("Clearing and entering password");
            waitForElementVisible(passwordInput);
            sendKeys(passwordInput, password);

            clickLoginButton();
        } catch (Exception e) {
            logger.error("Error during login process: {}", e.getMessage());
            throw new RuntimeException("Failed to perform login", e);
        }
    }

    /**
     * Login butonuna tıklar
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        waitForElementClickable(loginButton);
        click(loginButton);
    }

    /**
     * Login işleminin başarılı olup olmadığını kontrol eder
     * @return boolean
     */
    public boolean isLoginSuccessful() {
        try {
            logger.debug("Checking if login was successful");
            return wait.until(ExpectedConditions.visibilityOf(challengeNameLabel))
                    .isDisplayed();
        } catch (Exception e) {
            logger.warn("Login appears to be unsuccessful");
            return false;
        }
    }

    /**
     * Kullanıcı adının görünür olup olmadığını kontrol eder
     * @return boolean
     */
    public boolean isUsernameDisplayed() {
        try {
            return challengeNameLabel.isDisplayed();
        } catch (Exception e) {
            logger.warn("Username element is not visible");
            return false;
        }
    }

    /**
     * Hata mesajının görünür olup olmadığını kontrol eder
     * @return boolean
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hata mesajını döndürür
     * @return String hata mesajı
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return errorMessage.getText();
        }
        return "";
    }

    /**
     * Login sayfasında olup olmadığını kontrol eder
     * @return boolean
     */
    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    /**
     * Login form elementlerinin görünür olup olmadığını kontrol eder
     * @return boolean
     */
    public boolean areLoginElementsVisible() {
        try {
            return usernameInput.isDisplayed() &&
                    passwordInput.isDisplayed() &&
                    loginButton.isDisplayed();
        } catch (Exception e) {
            logger.warn("Login elements are not visible");
            return false;
        }
    }
}