package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends Page {
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Expected Inputs
    private static final String currency = "$";
    private static final String expectedCartURL = "https://www.saucedemo.com/checkout-step-one.html";
    private static final String expectedCheckoutURL = "https://www.saucedemo.com/checkout-step-two.html";

    // Locators for checkout page elements
    private static final By checkoutFormFN = By.id("first-name");
    private static final By checkoutFormLN = By.id("last-name");
    private static final By checkoutFormZIP = By.id("postal-code");
    private static final By checkoutFormContinueBtn = By.id("continue");
    private static final By listOfItemsPrice = By.className("inventory_item_price");
    private static final By subtotalText = By.className("summary_subtotal_label");
    private static final By finishButton = By.id("finish");

    // Method to verify redirection to the checkout step one page
    public boolean verifyRedirectionToStepOneCheckoutPage() {
        String actualCurrentURL = bot.getCurrentURL();
        return actualCurrentURL.equals(expectedCartURL);
    }

    // Method to verify redirection to the checkout step two page
    public boolean verifyRedirectionToStepTwoCheckoutPage() {
        String actualCurrentURL = bot.getCurrentURL();
        return actualCurrentURL.equals(expectedCheckoutURL);
    }


    // Method to fill the checkout form and continue to the next step
    public CheckoutPage fillCheckoutFormThenContinue(String firstName, String lastName, String postalCode) {
        bot.type(checkoutFormFN, firstName)
                .type(checkoutFormLN, lastName)
                .type(checkoutFormZIP, postalCode)
                .click(checkoutFormContinueBtn);
        return new CheckoutPage(driver);
    }

    // Method to verify the total amount of items before taxes
    public boolean verifyItemsTotalAmountBeforeTaxes() {
        boolean flag = false;
        double sum = 0;

        // Get the list of elements with the class name "inventory_item_price"
        for (WebElement element : bot.getListOfElements(listOfItemsPrice)) {
            String text = element.getText().replace(currency, ""); // Remove the dollar sign currency
            double number = Double.parseDouble(text); // Convert the string to a double
            sum += number; // Add the number to the sum
        }
        System.out.println("The sum of the numbers is: " + sum);

        // Get the subtotal text and remove the dollar sign
        String totalAmountText = bot.getText(subtotalText);
        String amount = totalAmountText.replaceAll("[^\\d.]", "");
        if (sum == Double.parseDouble(amount)) {
            System.out.println("The sum of the numbers is: " + sum);
            System.out.println("The total amount is: " + amount);
            flag = true;
        } else {
            System.out.println("The sum of the numbers is not equal to the total amount");
        }
        return flag;
    }

    // Method to click the finish button and navigate to the ThanksPage
    public ThanksPage clickFinishButton() {
        bot.click(finishButton);
        return new ThanksPage(driver);
    }


}
