package dev.tomasz.qa.tests.ui;

import dev.tomasz.qa.pages.InventoryPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Web shop")
@Feature("Cart")
public class CartTests extends BaseUiTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @Test(description = "Cart badge counts added products")
    public void cartBadgeCountsAddedProducts() {
        InventoryPage inventory = loggedInAsStandardUser()
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT);

        assertThat(inventory.cartCount()).isEqualTo(2);
    }

    @Test(description = "Removing a product updates the badge")
    public void removingProductUpdatesBadge() {
        InventoryPage inventory = loggedInAsStandardUser()
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT)
                .removeFromCart(BACKPACK);

        assertThat(inventory.cartCount()).isEqualTo(1);
    }

    @Test(description = "Cart page shows exactly the added products")
    public void cartShowsAddedProducts() {
        var cart = loggedInAsStandardUser()
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT)
                .openCart();

        assertThat(cart.itemNames()).containsExactlyInAnyOrder(BACKPACK, BIKE_LIGHT);
    }
}
