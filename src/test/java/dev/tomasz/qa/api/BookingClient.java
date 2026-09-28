package dev.tomasz.qa.api;

import dev.tomasz.qa.config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin client around the restful-booker endpoints. Tests assert on the returned Response. */
public class BookingClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(Config.apiBaseUrl())
            .setContentType(ContentType.JSON)
            // setAccept(ContentType.JSON) would send a long multi-value Accept header, which
            // restful-booker rejects with 418, so the plain media type is set explicitly.
            .addHeader("Accept", "application/json")
            .addFilter(new AllureRestAssured())
            .build();

    @Step("Request auth token")
    public String token() {
        return given().spec(spec)
                .body(Map.of("username", Config.get("api.user"), "password", Config.get("api.password")))
                .post("/auth")
                .then().statusCode(200)
                .extract().path("token");
    }

    @Step("Create booking")
    public Response create(Booking booking) {
        return given().spec(spec).body(booking).post("/booking");
    }

    @Step("Get booking {id}")
    public Response get(int id) {
        return given().spec(spec).get("/booking/{id}", id);
    }

    @Step("Update booking {id}")
    public Response update(int id, Booking booking, String token) {
        return given().spec(spec).cookie("token", token).body(booking).put("/booking/{id}", id);
    }

    @Step("Delete booking {id}")
    public Response delete(int id, String token) {
        return given().spec(spec).cookie("token", token).delete("/booking/{id}", id);
    }
}
