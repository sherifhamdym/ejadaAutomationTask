package engine;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import utility.PropertyManager;

import java.time.Duration;
import java.util.List;

public class Bot {
    // Attributes
    private final Wait<WebDriver> wait;
    private final PropertyManager prop = PropertyManager.getInstance();

    // Constructor using custom explicit wait
    public Bot(WebDriver driver) {
        this.wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NotFoundException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    // Type on element - clear then write
    public Bot type(By by, CharSequence text) {
        wait.until(d -> {
            d.findElement(by).clear();
            d.findElement(by).sendKeys(text);
            return true;
        });
        return this;
    }

    // click on element
    public Bot click(By by) {
        wait.until(d -> {
            d.findElement(by).click();
            return true;
        });
        return this;
    }

    // get text from element
    public String getText(By locator) {
        return wait.until(d -> {
            return d.findElement(locator).getText();
        });
    }

    // find list of elements
    public List<WebElement> getListOfElements(By locator) {
        return wait.until(d -> {
            return d.findElements(locator);
        });
    }

    // get alert
    public Alert getAlert() {
        return wait.until(d -> {
            return d.switchTo().alert();
        });
    }

    // get current url
    public String getCurrentURL() {
        return wait.until(d -> {
            return d.getCurrentUrl();
        });
    }

    // open a url from the properties file
    public Bot openURL_Saucedemo() {
        wait.until(d -> {
            String url = prop.getProperty("URL_SauceDemo");
            d.navigate().to(url);
            return true;
        });
        return this;
    }

    // check if an element is displayed
    public boolean isDisplayed(By by) {
        try {
            return wait.until(d -> d.findElement(by).isDisplayed());
        } catch (TimeoutException e) {
            return false;
        }
    }

}
