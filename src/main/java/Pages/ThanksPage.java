package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ThanksPage extends Page {
    public ThanksPage(WebDriver driver) {
        super(driver);
    }

    // Locators for the thanks page
    private static final By thanksPageHeader = By.className("complete-header");
    private static final By thanksPageBody = By.className("complete-text");

    // Method to get the header of thanks page
    public String validateThanksPageHeader() {
        return bot.getText(thanksPageHeader);
    }

    // Method to get the message of thanks page
    public String validateThanksPageBody() {
        return bot.getText(thanksPageBody);
    }


}
