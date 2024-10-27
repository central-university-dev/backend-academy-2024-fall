class PaymentProcessor:
    def process_payment(self, amount: float, method: str):
        if method == 'debit_card':
            print(f"Processing Debit Card payment of ${amount}")
        elif method == 'credit_card':
            print(f"Processing Credit Card payment of ${amount}")
        else:
            raise ValueError("Unsupported payment method")

    def refund_payment(self, amount: float, method: str):
        if method == 'debit_card':
            print(f"Processing Debit Card refund of ${amount}")
        elif method == 'credit_card':
            print(f"Processing Credit Card refund of ${amount}")
        else:
            raise ValueError("Unsupported payment method")


if __name__ == "__main__":
    processor = PaymentProcessor()
    processor.process_payment(100, 'debit_card')
    processor.refund_payment(50, 'credit_card')


from abc import ABC, abstractmethod


class ProcessMethod(ABC):
    @abstractmethod
    def debit():
        pass

    @abstractmethod
    def credit():
        pass

class ProccessorPayment(ProcessMethod):
    def debit(self, amount: float, method: str):
        print (f"Processing Debit Card payment of ${amount}")
    def card(self):
        print(f"Processing Credit Card payment of ${amount}")

class ProccessorRefund(ProcessMethod)
    def debit(self, amount: float, method: str):
        print (f"Processing Debit Card refund of ${amount}")
    def card(self):
        print(f"Processing Credit Card refund of ${amount}")


if __name__ == "__main__":
    processor = PaymentProcessor()
    processor.process_payment(100, 'debit_card')
    processor.refund_payment(50, 'credit_card')


from typing import Protocol


class IDebitCardManager(Protocol):
    def debit_payment(self) -> str:
        pass

    def debit_refund(self) -> str:
        pass


class ICreditCardManager(Protocol):
    def credit_payment(self) -> str:
        pass

    def credit_refund(self) -> str:
        pass


class CreditProcessor(ICreditCardManager):
    def credit_refund(self) -> str:
        ...

    def credit_payment(self) -> str:
        ...

# то же самое с DebitProcessor

from abc import ABC, abstractmethod


from abc import ABC, abstractmethod


class Method(ABC):

    @abstractmethod
    def do(self, amount : float) -> str:
        pass


class PaymentProcessor:

    def payment(self, amount: float, method: Method):
        method.do(amount)


if __name__ == "main":
    processor = PaymentProcessor()
    processor.process_payment(100, 'debit_card')
    processor.refund_payment(50, 'credit_card')