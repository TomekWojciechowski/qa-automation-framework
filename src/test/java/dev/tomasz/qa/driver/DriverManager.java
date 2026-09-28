package dev.tomasz.qa.driver;

import org.openqa.selenium.WebDriver;

/** Holds one WebDriver per thread so tests can run in parallel safely. */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            driver = DriverFactory.create();
            DRIVER.set(driver);
        }
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
