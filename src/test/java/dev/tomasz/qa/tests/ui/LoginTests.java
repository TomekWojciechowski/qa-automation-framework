package dev.tomasz.qa.tests.ui;

import dev.tomasz.qa.config.Config;
import dev.tomasz.qa.pages.InventoryPage;
import dev.tomasz.qa.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Web shop")
@Feature("Login")
public class LoginTests extends BaseUiTest {

    @Test(description = "Valid user reaches the product list")
    @Severity(SeverityLevel.BLOCKER)
    public void validUserCanLogIn() {
        InventoryPage inventory = loggedInAsStandardUser();

        assertThat(inventory.title()).isEqualTo("Products");
    }

    @Test(description = "Locked out user sees an error")
    @Severity(SeverityLevel.CRITICAL)
    public void lockedOutUserIsRejected() {
        LoginPage login = loginPage().submit(Config.get("ui.locked.user"), Config.get("ui.password"));

        assertThat(login.errorMessage()).contains("this user has been locked out");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"standard_user", "wrong_password", "Username and password do not match"},
                {"unknown_user", "secret_sauce", "Username and password do not match"},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"},
        };
    }

    @Test(dataProvider = "invalidCredentials", description = "Invalid credentials show the right message")
    @Severity(SeverityLevel.NORMAL)
    public void invalidCredentialsShowError(String user, String password, String expectedMessage) {
        LoginPage login = loginPage().submit(user, password);

        assertThat(login.errorMessage()).contains(expectedMessage);
    }
}
