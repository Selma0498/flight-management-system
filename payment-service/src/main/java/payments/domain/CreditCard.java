package payments.domain;

import payments.domain.enumeration.ECardType;

import java.util.Date;

public class CreditCard extends PaymentMethod {

    private ECardType cardType;
    private int cvc;
    private int cardNumber;
    private Date validityDate;

}
