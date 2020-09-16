package bookings.service.dto;

public class PaymentDTO {

    private String id;
    private String bookingNumber;
    private String toPay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

}
