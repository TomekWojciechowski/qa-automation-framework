package dev.tomasz.qa.driver;

import dev.tomasz.qa.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * Creates WebDriver instances. One driver per test thread (see {@link DriverManager}).
 * Local drivers are resolved automatically by Selenium Manager; a Selenium Grid or
 * standalone container is used when remote.url is set.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create() {
        String browser = Config.browser().toLowerCase();
        boolean headless = Config.headless();
        String remote = Config.remoteUrl();

        WebDriver driver = switch (browser) {
            case "chrome" -> {
                ChromeOptions options = chromeOptions(headless);
                yield remote == null ? new org.openqa.selenium.chrome.ChromeDriver(options) : remote(remote, options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                yield remote == null ? new org.openqa.selenium.firefox.FirefoxDriver(options) : remote(remote, options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        return driver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        // Disable Chrome's password-manager popups, which would block the demo shop tests.
        options.setExperimentalOption("prefs", java.util.Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        return options;
    }

    private static WebDriver remote(String url, org.openqa.selenium.Capabilities capabilities) {
        try {
            return new RemoteWebDriver(URI.create(url).toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid remote.url: " + url, e);
        }
    }
}
