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

}
