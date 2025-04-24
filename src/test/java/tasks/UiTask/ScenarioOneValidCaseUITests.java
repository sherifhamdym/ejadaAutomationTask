package tasks.UiTask;

import Pages.*;
import dataDriven.ExcelDataReader;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Epic("SauceDemo")
@Feature("Login - VALID Cases")
public class ScenarioOneValidCaseUITests extends TestBase {
    /**
     * Scenario One - Valid Full Scenario Cases UI Tests
     * Steps:
     * 1- Visit <a href="https://www.saucedemo.com/">...</a>
     * 2- Login with a valid username and password
     * 3- Verify that you’ve been logged in successfully and navigated to the products page
     * 4- Add the most expensive two products to your cart
     * 5- Click on the cart button
     * 6- Verify that you’ve been navigated to “Cart” page and the previously selected products are
     * displayed at the page
     * 7- Click on the “Checkout” button
     * 8- Verify that you’ve been navigated to the “Checkout” page
     * 9- Fill all the displayed form
     * 10- Click on the “Continue” button
     * 11- Verify the following:
     * a. Verify that you’ve been navigated to the “Overview” page
     * b. Verify the Items total amount before taxes
     * c. Verify that the URL matches with (<a href="https://www.saucedemo.com/checkout-steptwo.html">...</a>)
     * 12- Click on the “Finish” button
     * 13- Verify both, the (Thank You) and the (order has been dispatched) messages
     */

    // Test case: Full Scenario One - Valid Case
    @Story("E2E scenario for adding a products to the cart and checkout")
    @Test(description = "Given The user logged in successfully to sauce demo website, Data loaded from FullScenario_Data.xlsx and navigated to the products page, " +
            " When The user adds the most expensive two products to the cart, " +
            " And The user clicks on the cart button, " +
            " And The user clicks on the checkout button, " +
            " And The user fills all the displayed form, " +
            " And The user clicks on the continue button, " +
            " And The user clicks on the finish button, " +
            " Then Verify that the thank you message and order dispatched message are displayed"
            , dataProvider = "FullScenarioTestData", dataProviderClass = ExcelDataReader.class)
    @Parameters({"validUsername", "validPassword","numberOfItemsToAdd","firstname","lastname","postalCode","expectedThanksHeader","expectedThanksBody"})
    public void FullScenarioOne(String validUsername,String validPassword,String numberOfItemsToAdd,String firstname,String lastname,String postalCode,String expectedThanksHeader,String expectedThanksBody) {

        //Step 1: Visit the login page
        boolean isLoggedInSuccessfully = new LoginPage(getDriver())
                .loginWithValid(validUsername, validPassword);

        Assert.assertTrue(isLoggedInSuccessfully, "Login was not successful");

        //Step 2: Verify navigated to the products page and Add the most expensive two products to your cart
        boolean isMyCartDisplayedSuccessfully = new ProductPage(getDriver())
                .addTwoMostExpensiveProductsToCart(numberOfItemsToAdd)
                .clickOnCartButton()
                .verifyRedirectionToMyCart();
        Assert.assertTrue(isMyCartDisplayedSuccessfully, "Failed to navigate to cart page");

        //Step 3: Verify that the previously selected products are displayed at the page
        String cartItemsSize = new YourCartPage(getDriver())
                .verifyProductsAddedInCart();

        Assert.assertEquals(cartItemsSize, numberOfItemsToAdd, "Cart should contain 2 items");

        //Step 4: Verify redirection to check out step one, after click on the “Checkout” button
        boolean isCheckoutStepOneDisplayedSuccessfully = new YourCartPage(getDriver())
                .clickOnCheckoutButton()
                .verifyRedirectionToStepOneCheckoutPage();
        Assert.assertTrue(isCheckoutStepOneDisplayedSuccessfully, "Failed to navigate to checkout page - Step One");

        //Step 5: Verify the total amount before tax, after filling the form and clicking on continue button
        boolean TotalAmountIsCorrect = new CheckoutPage(getDriver())
                .fillCheckoutFormThenContinue(firstname, lastname, postalCode)
                .verifyItemsTotalAmountBeforeTaxes();
        Assert.assertTrue(TotalAmountIsCorrect, "Total amount is not correct");

        //Step 6: Verify redirection to check out step two
        boolean isCheckoutStepTwoDisplayedSuccessfully = new CheckoutPage(getDriver())
                .verifyRedirectionToStepTwoCheckoutPage();
        Assert.assertTrue(isCheckoutStepTwoDisplayedSuccessfully, "Failed to navigate to checkout page - Step Two");

        //Step 7:Verify Thanks page header message, after clicking on finish button
        String actualThanksHeader = new CheckoutPage(getDriver())
                .clickFinishButton()
                .validateThanksPageHeader();
//        String expectedThanksHeader = "Thank you for your order!";
        Assert.assertEquals(actualThanksHeader, expectedThanksHeader, "Thank you message is not as expected");

        //Step 8:Verify Thanks page body message, after clicking on finish button
        String actualThanksBody = new ThanksPage(getDriver())
                .validateThanksPageBody();
//        String expectedThanksBody = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
        Assert.assertEquals(actualThanksBody, expectedThanksBody, "Order dispatched message is not as expected");

    }
}
