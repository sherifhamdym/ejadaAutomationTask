package Pages;

import engine.Bot;
import org.openqa.selenium.WebDriver;

public abstract class Page {
    // Attributes
    protected final WebDriver driver;
    protected Bot bot;

    // Constructor
    public Page(WebDriver driver) {
        this.driver = driver;
        this.bot = new Bot(driver);
    }
}
