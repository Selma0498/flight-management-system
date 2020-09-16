package notifications.service;

import notifications.domain.enumeration.ENotificationType;
import notifications.service.dto.BookingDTO;
import notifications.service.dto.FlightDTO;
import notifications.service.exceptions.EmailServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private String notificationMessage = "";

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendBookingInfo(BookingDTO bookingDTO, ENotificationType notificationType) {
        if(notificationType.equals(ENotificationType.BOOKING_CANCELLED)) {
            notificationMessage = "This information is for a passenger with passenger id: " + bookingDTO.getPassengerId() + "" +
                ". If this concerns you, please read on. " +
                "Dear Sir/Madam, the booking with booking number: " + bookingDTO.getBookingNumber() +
                " for flight with flight number: " + bookingDTO.getFlightNumber() + " has been cancelled. Best regards!";
        } else if(notificationType.equals(ENotificationType.BOOKING_CONFIRMED)) {
            notificationMessage = "This information is for a passenger with passenger id: " + bookingDTO.getPassengerId() + "" +
                ". If this concerns you, please read on. " +
                "Dear Sir/Madam, the booking with booking number: " + bookingDTO.getBookingNumber() +
                " for flight with flight number: " + bookingDTO.getFlightNumber() +
                " has been confirmed. Safe travels and best regards!";
        }
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("selmagolos@yahoo.com");
            message.setSubject("Booking Update: " + bookingDTO.getBookingNumber());
            message.setText(notificationMessage);
            message.setFrom("Notification Service");
            emailSender.send(message);
        } catch (Exception e) {
            throw new EmailServiceException(e);
        }
    }

    public void sendFlightInfo(FlightDTO flightDTO, ENotificationType notificationType) {
        String notificationMessage = "";
        if(notificationType.equals(ENotificationType.FLIGHT_CANCELLED)) {
            notificationMessage = "Dear Sir/Madam, the flight with flight number: " + flightDTO.getFlightNumber() +
                " for origin: " + flightDTO.getOrigin() + " and destination: " + flightDTO.getDestination() +
                " with departure date on: " + flightDTO.getDepartureDate() + " has been cancelled. Best regards!";
        } else if(notificationType.equals(ENotificationType.FLIGHT_UPDATED)) {
            notificationMessage = "Dear Sir/Madam, the flight with flight number: " + flightDTO.getFlightNumber() +
                " for origin: " + flightDTO.getOrigin() + " and destination: " + flightDTO.getDestination() +
                " with departure date on: " + flightDTO.getDepartureDate() +
                " has been updated. Safe travels and best regards!";
        }
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("selmagolos@yahoo.com");
            message.setSubject("Flight Update: " + flightDTO.getFlightNumber());
            message.setText(notificationMessage);
            message.setFrom("notification@mail.com");
            emailSender.send(message);
        } catch (Exception e) {
            throw new EmailServiceException(e);
        }
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
}
