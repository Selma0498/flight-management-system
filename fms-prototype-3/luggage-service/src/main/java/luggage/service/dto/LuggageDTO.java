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

    public String getLuggageType() {
        return luggageType;
    }

    public void setLuggageType(String luggageType) {
        this.luggageType = luggageType;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }
}
