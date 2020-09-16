package flights.service.dto;

import flights.domain.Flight;

public class FlightDTO {

    private String flightNumber;
    private String departureDate;
    private String origin;
    private String destination;

    public FlightDTO(Flight flight) {
        this.flightNumber = flight.getFlightNumber();
        this.departureDate = flight.getDepartureDate().toString();
        this.origin = flight.getOrigin().toString();
        this.destination = flight.getDestination().toString();
    }

}
