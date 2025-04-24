package tasks.UiTask;

import Pages.LoginPage;
import dataDriven.ClassDataReader;
import dataDriven.ExcelDataReader;
import dataDriven.JsonDataReader;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Epic("SauceDemo")
@Feature("Login - INVALID Cases")
public class ScenarioOneInvalidCasesUITests extends TestBase {

    /**
     * Scenario One - Invalid Cases UI Tests
     * Steps:
     * 1. Visit <a href="https://www.saucedemo.com/">...</a>
     * 2. Login with invalid credentials
     * 3. Verify each error message
     * NOTE:
     * The test cases within this TestClass are for handling invalid cases for the above scenario,
     * with different data driven approaches:[a class with properties file], [JSON file], and [Excel sheet].
     */

    // First approach - Data driven from a class with properties file
    @Story("User logs in with incorrect credentials using Class and properties file")
    @Test(description = "Given Visit https://www.saucedemo.com/, " +
            " And Load data from a class with properties file " +
            " When Login with invalid credentials " +
            "Then Verify each error message", dataProvider = "loginData", dataProviderClass = ClassDataReader.class)
    @Parameters({"username", "password","Expected Error Message"})
    public void loginWithInvalidCredentials_Class(String username, String password, String expectedErrorMessage) {

        Assert.assertEquals(loginWithInvalidCredentials(username, password), expectedErrorMessage,
                "Error message is not as expected - loginWithInvalidCredentials_Class ");
    }

    // Second approach - Data driven from a JSON file
    @Story("User logs in with incorrect credentials using JSON file")
    @Test(description = "Given Visit https://www.saucedemo.com/, " +
            " And Load data from from a JSON file " +
            " When Login with invalid credentials " +
            "Then Verify each error message", dataProvider = "loginDataFromJson", dataProviderClass = JsonDataReader.class)
    @Parameters({"username", "password","Expected Error Message"})
    public void loginWithInvalidCredentials_JSON(String username, String password, String expectedErrorMessage) {
        Assert.assertEquals(loginWithInvalidCredentials(username, password), expectedErrorMessage,
                "Error message is not as expected - loginWithInvalidCredentials_JSON");
    }

    // Third approach - Data driven from an Excel sheet
    @Story("User logs in with incorrect credentials using Excel file")
    @Test(description = "Given Visit https://www.saucedemo.com/, " +
            " And Load data from from an Excel sheet " +
            " When Login with invalid credentials " +
            "Then Verify each error message", dataProvider = "loginDataFromExcel", dataProviderClass = ExcelDataReader.class)
    @Parameters({"username", "password","Expected Error Message"})
    public void loginWithInvalidCredentials_XLSX( String username, String password,String expectedErrorMessage) {
        Assert.assertEquals(loginWithInvalidCredentials(username, password), expectedErrorMessage,
                "Error message is not as expected - loginWithInvalidCredentials_XLSX");
    }

    // General method used for the tests to log in with invalid credentials
    private String loginWithInvalidCredentials(String username, String password) {
        return new LoginPage(getDriver())
                .loginWithInvalid(username, password);
    }
}
