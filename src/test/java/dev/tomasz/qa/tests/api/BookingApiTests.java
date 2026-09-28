package dev.tomasz.qa.tests.api;

import dev.tomasz.qa.api.Booking;
import dev.tomasz.qa.api.BookingClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking API")
@Feature("Booking lifecycle")
public class BookingApiTests {

    private BookingClient client;
    private String token;

    @BeforeClass
    public void setUp() {
        client = new BookingClient();
        token = client.token();
    }

    @Test(description = "Created booking can be read back unchanged")
    @Severity(SeverityLevel.BLOCKER)
    public void createdBookingCanBeReadBack() {
        Booking booking = Booking.sample();

        Response created = client.create(booking);
        assertThat(created.statusCode()).isEqualTo(200);
        int id = created.path("bookingid");

        Booking loaded = client.get(id).then().statusCode(200).extract().as(Booking.class);
        assertThat(loaded).isEqualTo(booking);
    }

    @Test(description = "Authenticated update changes the price")
    @Severity(SeverityLevel.CRITICAL)
    public void updateChangesPrice() {
        int id = client.create(Booking.sample()).path("bookingid");

        Response updated = client.update(id, Booking.sample().withTotalprice(999), token);

        assertThat(updated.statusCode()).isEqualTo(200);
        assertThat(client.get(id).<Integer>path("totalprice")).isEqualTo(999);
    }

    @Test(description = "Update without a valid token is forbidden")
    @Severity(SeverityLevel.CRITICAL)
    public void updateWithoutValidTokenIsForbidden() {
        int id = client.create(Booking.sample()).path("bookingid");

        Response response = client.update(id, Booking.sample().withTotalprice(1), "invalid-token");

        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test(description = "Deleted booking is no longer found")
    @Severity(SeverityLevel.NORMAL)
    public void deletedBookingIsGone() {
        int id = client.create(Booking.sample()).path("bookingid");

        // restful-booker answers 201 to a successful DELETE
        assertThat(client.delete(id, token).statusCode()).isEqualTo(201);
        assertThat(client.get(id).statusCode()).isEqualTo(404);
    }
}
