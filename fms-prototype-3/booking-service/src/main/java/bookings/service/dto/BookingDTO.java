package bookings.service.dto;

import bookings.domain.Booking;

public class BookingDTO {

    private Integer bookingNumber;
    private String flightNumber;
    private String passengerId;

    public BookingDTO(Booking booking) {
        this.bookingNumber = booking.getBookingNumber();
        this.flightNumber = booking.getFlightNumber();
        this.passengerId = booking.getPassengerId();
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
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
