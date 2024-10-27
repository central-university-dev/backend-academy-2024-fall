import abc
from typing import Protocol, Any


class Loggable(Protocol):
    @abc.abstractmethod
    def log(self) -> None:
        pass


class MyClass(Loggable):
    @abc.abstractmethod
    def log_2(self) -> None:
        pass


class SecondLoggable:

    def log(self) -> None:
        print("My second log")


def send_logs(obj: Loggable):
    obj.log()


if __name__ == '__main__':
    not_loggable = object()

    send_logs(MyClass())
    send_logs(SecondLoggable())
    send_logs(not_loggable)
