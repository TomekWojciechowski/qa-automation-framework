package dev.tomasz.qa.tests.ui;

import dev.tomasz.qa.config.Config;
import dev.tomasz.qa.driver.DriverManager;
import dev.tomasz.qa.pages.InventoryPage;
import dev.tomasz.qa.pages.LoginPage;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

public abstract class BaseUiTest {

    // One test class instance is shared by all parallel methods, so the driver is
    // always fetched from the thread-local DriverManager and never stored in a field.
    protected WebDriver driver() {
        return DriverManager.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE && driver() instanceof TakesScreenshot shot) {
                byte[] png = shot.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot on failure", "image/png", new ByteArrayInputStream(png), ".png");
            }
        } finally {
            DriverManager.quit();
        }
    }

    protected LoginPage loginPage() {
        return new LoginPage(driver()).open();
    }

    protected InventoryPage loggedInAsStandardUser() {
        return loginPage().loginAs(Config.get("ui.user"), Config.get("ui.password"));
    }
}
