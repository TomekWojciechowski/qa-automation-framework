package dev.tomasz.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By FINISH = By.id("finish");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By TOTAL = By.className("summary_total_label");
    private static final By CONFIRMATION = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter customer data: {firstName} {lastName}, {postalCode}")
    public CheckoutPage enterCustomerData(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE);
        return this;
    }

    @Step("Continue without customer data")
    public CheckoutPage continueEmpty() {
        click(CONTINUE);
        return this;
    }

    public String errorMessage() {
        return textOf(ERROR);
    }

    public String totalLabel() {
        return textOf(TOTAL);
    }

    @Step("Finish order")
    public String finishOrder() {
        click(FINISH);
        return textOf(CONFIRMATION);
    }
}
