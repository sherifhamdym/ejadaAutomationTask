package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YourCartPage extends Page {
    public YourCartPage(WebDriver driver) {
        super(driver);
    }
    private static final String expectedCartURL = "https://www.saucedemo.com/cart.html";
    private static final By cartItemsList = By.className("cart_item");
    private static final By checkoutBTN = By.id("checkout");

    // Check the redirection to my cart screen
    public boolean verifyRedirectionToMyCart() {
        String actualCurrentURL = bot.getCurrentURL();
        return actualCurrentURL.equals(expectedCartURL);

    }
    // Check the products (2) items are in the cart
    public String  verifyProductsAddedInCart() {
        return (String.valueOf(bot.getListOfElements(cartItemsList).size()));
    }

    // click on checkout button to go to the next page
    public CheckoutPage clickOnCheckoutButton() {
        bot.click(checkoutBTN);
        return new CheckoutPage(driver);
    }

}
