from abc import ABC, abstractmethod


# Single Responsibility Principle (SRP)
class PaymentProcessor(ABC):
    @abstractmethod
    def process_payment(self, amount: float) -> str:
        pass


# В каждом методе process_payment конкретного процессора все действия находятся на одном уровне абстракции.
# Мы не смешиваем разные уровни за счет того, что логика обработки и логирования вынесена в отдельный метод
# _log_payment. Это делает код более понятным.
class DebitCardPaymentProcessor(PaymentProcessor):

    # Каждый метод выполняет ограниченное количество операций, что облегчает понимание и сопровождение кода.
    # Метод process_payment теперь отвечает только за вызов _log_payment, а все
    # действия по логирование остаются внутри одного метода.
    def process_payment(self, amount: float):
        return self._log_payment("DebitCard", amount)

    def _log_payment(self, method: str, amount: float):
        print(f"Processing {method} payment of ${amount}")


class CreditCardPaymentProcessor(PaymentProcessor):
    def process_payment(self, amount: float):
        return self._log_payment("Credit Card", amount)

    def _log_payment(self, method: str, amount: float):
        print(f"Processing {method} payment of ${amount}")


class PaymentService:
    def __init__(self, processor: PaymentProcessor):
        self.processor = processor

    def make_payment(self, amount: float):
        self.processor.process_payment(amount)


# Dependency Inversion Principle (DIP)
if __name__ == "__main__":
    debit_card_processor = DebitCardPaymentProcessor()
    credit_card_processor = CreditCardPaymentProcessor()

    payment_service = PaymentService(debit_card_processor)
    payment_service.make_payment(100)

    payment_service = PaymentService(credit_card_processor)
    payment_service.make_payment(150)


