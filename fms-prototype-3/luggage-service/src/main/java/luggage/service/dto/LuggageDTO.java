package luggage.service.dto;

import luggage.domain.Luggage;

public class LuggageDTO {

    private String luggageType;
    private String bookingNumber;
    private String passengerId;
    private String rfidTag;

    public LuggageDTO(Luggage luggage) {
        this.luggageType = luggage.getLuggageType().name();
        this.bookingNumber = luggage.getBookingNumber().toString();
        this.passengerId = luggage.getPassengerId();
        this.rfidTag = luggage.getRfidTag();
    }
}
