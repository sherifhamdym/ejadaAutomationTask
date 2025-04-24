package Pages;

import engine.Bot;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Page {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Locators for the login page
    private static final By usernameField = By.id("user-name");
    private static final By passwordField = By.id("password");
    private static final By loginButton = By.id("login-button");
    private static final By errorMessage = By.xpath("//h3");
    private static final By burgerMenu = By.id("react-burger-menu-btn");


    // Method to validate credentials with Valid data
    @Step("Valid Login with username: {0} and password: {1}")
    public boolean loginWithValid(String username, String password) {
        return login(username, password).isDisplayed(burgerMenu);
    }

    // Method to validate credentials with Invalid data
    @Step("InValid Login with username: {0} and password: {1}")
    public String loginWithInvalid(String username, String password) {
        return login(username, password).getText(errorMessage);
    }

    // Called method for valid & invalid login
    private Bot login(String username, String password) {
        return bot
                .openURL_Saucedemo()
                .type(usernameField, username)
                .type(passwordField, password)
                .click(loginButton);

    }

}
