from abc import ABC, abstractmethod


# Single Responsibility Principle (SRP)
class PaymentProcessor(ABC):
    @abstractmethod
    def process_payment(self, amount: float):
        pass


# Interface Segregation Principle (ISP)
class RefundablePaymentProcessor(PaymentProcessor):
    @abstractmethod
    def refund_payment(self, amount: float):
        pass


class DebitCardPaymentProcessor(PaymentProcessor):
    def process_payment(self, amount: float):
        print(f"Processing payment of ${amount}")


class CreditCardPaymentProcessor(PaymentProcessor):
    def process_payment(self, amount: float):
        print(f"Processing Credit Card payment of ${amount}")


# Open/Closed Principle (OCP)
class PaymentService:
    def __init__(self, processor: RefundablePaymentProcessor):
        self.processor = processor

    def make_payment(self, amount: float):
        self.processor.process_payment(amount)


# Liskov Substitution Principle (LSP)
def process_payment(service: PaymentService, amount: float):
    service.make_payment(amount)


class RefundableDebitCardPaymentProcessor(DebitCardPaymentProcessor, RefundablePaymentProcessor):
    def refund_payment(self, amount: float):
        print(f"Processing refund of ${amount} on Debit card")


class RefundableCreditCardPaymentProcessor(CreditCardPaymentProcessor, RefundablePaymentProcessor):
    def refund_payment(self, amount: float):
        print(f"Processing refund of ${amount} on Credit Card")


# Dependency Inversion Principle (DIP)
if __name__ == "__main__":
    debit_card_processor = RefundableDebitCardPaymentProcessor()
    credit_card_processor = RefundableCreditCardPaymentProcessor()

    payment_service = PaymentService(debit_card_processor)
    payment_service.make_payment(100)
    payment_service.processor.refund_payment(50)

    payment_service = PaymentService(credit_card_processor)
    payment_service.make_payment(150)
    payment_service.processor.refund_payment(75)
