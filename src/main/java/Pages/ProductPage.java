package Pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

public class ProductPage extends Page {
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // Number of items to add to cart initially two
    int numberOfItems = 2;
    String currency="$";
    // Locators for elements on the product page
    private static final By shoppingCartBTN = By.className("shopping_cart_link");
    private static final By productItemName = By.className("inventory_item_name");
    private static final By addToCartBTN = By.tagName("button");

    private void attemptToHandleChangePasswordPopup() {
        try {
            // Give some time for the alert to appear
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).perform();
            actions.sendKeys(Keys.ESCAPE).perform();
            System.out.println("Attempted to click Enter on a potential change password popup.");

        } catch (Exception e) {
            System.out.println("No change password alert detected or Robot interaction failed.");
        }
    }

    // Add the two most expensive products to cart
    @Step("Adding Most Expensive Products to cart: Number of products = {0}")
    public ProductPage addTwoMostExpensiveProductsToCart(String numberOfItemsToAdd) {
        // This is just a workaround to handle the change password popup that may appear when the page loads after login, as it always
        // appears even after configuring the browser options in the TestBase to not show it
        attemptToHandleChangePasswordPopup();

        // Get the number "firstTwo" and assign to the global variable
        numberOfItems = Integer.parseInt(numberOfItemsToAdd);

        // Find all product elements
        List<WebElement> inventoryItems = driver.findElements(By.className("inventory_item"));

        // Create a map to store prices and corresponding WebElements
        Map<Double, WebElement> priceToItemMap = new HashMap<>();
        List<Double> prices = new ArrayList<>();

        // Extract product information (price and element)
        for (WebElement item : inventoryItems) {
            String priceText = item.findElement(By.className("inventory_item_price")).getText().replace(currency, "");
            double price = Double.parseDouble(priceText);

            // Store price and corresponding WebElement
            priceToItemMap.put(price, item);
            // Add price to the list
            prices.add(price);
        }

        // Sort prices in descending order
        prices.sort(Comparator.reverseOrder());

        // Get the two most expensive products
        addMostExpensiveProduct(priceToItemMap, prices, numberOfItems);

        return new ProductPage(driver);
    }

    // View the Shopping Cart
    public YourCartPage clickOnCartButton() {
        bot.click(shoppingCartBTN);
        return new YourCartPage(driver);
    }

    // Private called method to Add the most expensive products to cart logic
    private void addMostExpensiveProduct(Map<Double, WebElement> priceToItemMap, List<Double> prices, int numberOfItems) {
        // Add the two most expensive products to cart
        for (int i = 0; i < numberOfItems; i++) {
            double price = prices.get(i);
            WebElement item = priceToItemMap.get(price);

            // Get product name for logging
            String name = item.findElement(productItemName).getText();

            // Find and click add to cart button
            WebElement addToCartButton = item.findElement(addToCartBTN);
            addToCartButton.click();

            System.out.println("Added to cart: " + name + " - "+ currency + price);
        }
    }
}
