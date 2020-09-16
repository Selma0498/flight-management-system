package bookings.service.dto;

import bookings.domain.Booking;

public class BookingDTO {

    private String bookingNumber;
    private String flightNumber;
    private String passengerId;

    public BookingDTO(Booking booking) {
        this.bookingNumber = booking.getBookingNumber().toString();
        this.flightNumber = booking.getFlightNumber();
        this.passengerId = booking.getPassengerId();
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
}
