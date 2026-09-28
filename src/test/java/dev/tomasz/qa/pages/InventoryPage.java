package dev.tomasz.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By TITLE = By.className("title");
    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By ITEM_PRICES = By.className("inventory_item_price");
    private static final By SORT = By.className("product_sort_container");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_LINK = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return textOf(TITLE);
    }

    public List<String> productNames() {
        return all(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public List<Double> productPrices() {
        return all(ITEM_PRICES).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    @Step("Add '{productName}' to cart")
    public InventoryPage addToCart(String productName) {
        click(By.id("add-to-cart-" + slug(productName)));
        return this;
    }

    @Step("Remove '{productName}' from cart")
    public InventoryPage removeFromCart(String productName) {
        click(By.id("remove-" + slug(productName)));
        return this;
    }

    @Step("Sort products by '{option}'")
    public InventoryPage sortBy(String option) {
        new Select(visible(SORT)).selectByVisibleText(option);
        return this;
    }

    public int cartCount() {
        return isPresent(CART_BADGE) ? Integer.parseInt(textOf(CART_BADGE)) : 0;
    }

    @Step("Open cart")
    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    private static String slug(String productName) {
        return productName.toLowerCase().replace(' ', '-');
    }
}
