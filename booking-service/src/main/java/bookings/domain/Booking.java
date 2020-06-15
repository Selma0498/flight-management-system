package bookings.domain;

import bookings.domain.enumeration.EBookingState;

public class Booking {

    private int bookingNumber;
    private String flightNumber;
    private String passengerId;
    private int invoiceNumber;
    private boolean invoiceSet;
    private EBookingState bookingState;
    private BoardingPass boardingPass;
    private double price;

}
