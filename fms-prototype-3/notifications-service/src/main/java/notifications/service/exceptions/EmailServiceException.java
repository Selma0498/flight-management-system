package notifications.service.exceptions;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException(Exception e) {
        super(e.getMessage());
    }
}
