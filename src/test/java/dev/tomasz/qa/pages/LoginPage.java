package dev.tomasz.qa.pages;

import dev.tomasz.qa.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open login page")
    public LoginPage open() {
        driver.get(Config.uiBaseUrl());
        return this;
    }

    @Step("Log in as {username}")
    public InventoryPage loginAs(String username, String password) {
        submit(username, password);
        return new InventoryPage(driver);
    }

    @Step("Submit credentials for {username}")
    public LoginPage submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
        return this;
    }

    public String errorMessage() {
        return textOf(ERROR);
    }
}
