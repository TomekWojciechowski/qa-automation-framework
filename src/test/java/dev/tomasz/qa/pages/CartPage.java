package dev.tomasz.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By CHECKOUT = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        visible(CHECKOUT);
        return all(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkout() {
        click(CHECKOUT);
        return new CheckoutPage(driver);
    }
}
