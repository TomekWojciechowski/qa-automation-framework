package dev.tomasz.qa.api;

/** Request/response model of the restful-booker API. */
public record Booking(String firstname,
                      String lastname,
                      int totalprice,
                      boolean depositpaid,
                      BookingDates bookingdates,
                      String additionalneeds) {

    public record BookingDates(String checkin, String checkout) {
    }

    public static Booking sample() {
        return new Booking("Anna", "Kowalska", 150, true,
                new BookingDates("2026-11-01", "2026-11-05"), "Breakfast");
    }

    public Booking withTotalprice(int newPrice) {
        return new Booking(firstname, lastname, newPrice, depositpaid, bookingdates, additionalneeds);
    }
}
