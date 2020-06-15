package bookings.domain;

import bookings.domain.enumeration.EFlightType;

import java.sql.Timestamp;
import java.util.Date;

public class BoardingPass {

    private String passengerName;
    private String origin;
    private String destination;
    private String gateNumber;
    private Date departureDate;
    private Timestamp boardingTime;
    private String flightNumber;
    private EFlightType flightType;
    private String seatNumber;

    //calculate boarding time

    //to string to create a boarding pass to preview to user
}
