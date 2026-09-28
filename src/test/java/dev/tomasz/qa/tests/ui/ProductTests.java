package dev.tomasz.qa.tests.ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Web shop")
@Feature("Product list")
public class ProductTests extends BaseUiTest {

    @Test(description = "Shop lists six products")
    public void shopListsSixProducts() {
        assertThat(loggedInAsStandardUser().productNames()).hasSize(6);
    }

    @Test(description = "Sorting by price low to high orders prices ascending")
    public void sortByPriceAscending() {
        List<Double> prices = loggedInAsStandardUser()
                .sortBy("Price (low to high)")
                .productPrices();

        assertThat(prices).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test(description = "Sorting by name Z to A orders names descending")
    public void sortByNameDescending() {
        List<String> names = loggedInAsStandardUser()
                .sortBy("Name (Z to A)")
                .productNames();

        assertThat(names).isSortedAccordingTo(Comparator.reverseOrder());
    }
}
