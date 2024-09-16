package backend.academy.records;

public class PaymentHandler {

    public void handlePayment(PaymentRequest paymentRequest) {
        // ...

        System.out.println(STR."""
            Payment to \{paymentRequest.cardNumber()}
            in amount of \{paymentRequest.amount()}
            is handled suscessfully
            """
        );
    }
}
