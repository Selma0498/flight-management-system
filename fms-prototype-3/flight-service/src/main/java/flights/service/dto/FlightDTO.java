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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
