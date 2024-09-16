package backend.academy.dto;

public class PaymentHandler {

    public void handlePayment(PaymentRequest paymentRequest) {
        // ...

        System.out.println(STR."""
            Payment to \{paymentRequest.getCardNumber()}
            in amount of \{paymentRequest.getAmount()}
            is handled suscessfully
            """
        );
    }
}
