package tasks.UiTask;

import engine.Bot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import utility.PropertyManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class TestBase {
    //protected WebDriver driver;
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected Bot bot;
    private final PropertyManager prop = PropertyManager.getInstance();

    /**
     * Method to initialize the WebDriver and Bot instance.
     * This method will be called before each test method,
     * using TestNG @BeforeMethod annotation to ensure it runs before each test,
     * and @Parameters to accept browser parameter from testng.xml.
     * The @Optional annotation allows the method to accept a null or empty value for the browser parameter.
     * If the browser parameter is not provided, it will use the default value from the config.properties file.
     * The method also sets up the WebDriver instance based on the provided or default browser,
     * and initializes the Bot instance with the WebDriver.
     */
    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("") String browser) {
        // check if the browser parameter is provided in testng.xml
        String browserName;
        if (browser == null || browser.isEmpty()) {
            browserName = prop.getProperty("browser");
            System.out.println("Browser parameter not provided in testng.xml, using value from config.properties: " + browserName);
        } else {
            browserName = browser;
            System.out.println("Setting up browser from testng.xml: " + browserName);
        }
        openBrowser(browserName);
        bot = new Bot(getDriver());
    }

    // ensures that the WebDriver is closed after each test method
    // and the ThreadLocal variable is removed to prevent memory leaks
    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    // Method to get the WebDriver instance
    public WebDriver getDriver() {
        return driver.get();
    }

    // Method to set the WebDriver instance
    private void openBrowser(String browser) {
        Map<String, Object> prefs = new HashMap<>();
        switch (browser.toLowerCase()) {
            case "chrome":
                // driver initialization for Chrome
                ChromeOptions options;
                options = new ChromeOptions();
                options.setBrowserVersion("134"); // desired Chrome version
                options.addArguments("start-maximized"); //  maximize the window
                options.addArguments("-headless"); //  run in headless mode
                // Disable caching
                options.addArguments("--disable-cache");
                options.addArguments("--disk-cache-size=0");
                options.addArguments("--disable-http-cache");
                // Disable saving passwords
                prefs.put("autofill.profile_enabled", false);
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                //
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-web-security");
                driver.set(new ChromeDriver(options)); // set the driver to ThreadLocal
                break;
            case "firefox":
                // driver initialization for Firefox
                FirefoxOptions firefoxOptions;
                firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-headless");
                firefoxOptions.setBrowserVersion("134"); // desired Firefox version
                driver.set(new FirefoxDriver(firefoxOptions));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser); //
        }
        //getDriver().manage().window().maximize(); // already defined in options
    }

    // Method to clean up Allure results before the test suite starts
    @BeforeSuite
    public static void cleanAllureResults() throws IOException {
        Path allureResults = Paths.get("allure-results");
        if (Files.exists(allureResults)) {
            Files.walk(allureResults)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}

