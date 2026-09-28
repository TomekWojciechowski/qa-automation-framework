package dev.tomasz.qa.tests.ui;

import dev.tomasz.qa.pages.CheckoutPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Web shop")
@Feature("Checkout")
public class CheckoutTests extends BaseUiTest {

    private static final String BACKPACK = "Sauce Labs Backpack";

    @Test(description = "Customer completes an order end to end")
    @Severity(SeverityLevel.BLOCKER)
    public void customerCanCompleteOrder() {
        CheckoutPage checkout = loggedInAsStandardUser()
                .addToCart(BACKPACK)
                .openCart()
                .checkout()
                .enterCustomerData("Anna", "Kowalska", "02826");

        assertThat(checkout.totalLabel()).isEqualTo("Total: $32.39");
        assertThat(checkout.finishOrder()).isEqualTo("Thank you for your order!");
    }

    @Test(description = "Checkout requires customer data")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutRequiresCustomerData() {
        CheckoutPage checkout = loggedInAsStandardUser()
                .addToCart(BACKPACK)
                .openCart()
                .checkout()
                .continueEmpty();

        assertThat(checkout.errorMessage()).contains("First Name is required");
    }
}
