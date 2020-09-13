package payments.service.dto;

import payments.domain.Payment;

public class PaymentDTO {

    private Long id;
    private Integer bookingNumber;
    private Double toPay;

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.bookingNumber = payment.getBookingNumber();
        this.toPay = payment.getToPay();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Double getToPay() {
        return toPay;
    }

    public void setToPay(Double toPay) {
        this.toPay = toPay;
    }
}
