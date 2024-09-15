import time
import uuid
from abc import ABC, abstractmethod
from threading import Timer


class DomainModel:
    oid: uuid.UUID = uuid.uuid4()
    object_name: str

    def __init__(self, object_name: str, oid: uuid.UUID = None):
        self.object_name = object_name
        if oid:
            self.oid = oid

    def __str__(self) -> str:
        return f"DomainModel ({self.oid}, {self.object_name})"


class Sender(ABC):

    @abstractmethod
    def send(self, obj: DomainModel) -> None:
        """Отправка данных обьекта."""


class LoggerMixin:

    def _log(self, message: str) -> None:
        print(f"[LOG] {self} {message}")


class TimerMixin:
    def __init__(self) -> None:
        self._start_time = 0
        self._end_time = 0

    def _set_start_time(self, start_time: float) -> None:
        self._start_time = start_time

    def _set_end_time(self, end_time: float) -> None:
        self._end_time = end_time

    def get_exec_time(self):
        return self._end_time - self._start_time


class ModelSender(Sender, LoggerMixin, TimerMixin):

    def send(self, obj: DomainModel) -> None:
        self._log("START SENDING")
        self._set_start_time(time.time())
        print(obj)
        time.sleep(1)
        self._set_end_time(time.time())
        self._log("END SENDING")


if __name__ == '__main__':
    model_sender = ModelSender()

    model_sender.send(DomainModel("test"))
    print(model_sender.get_exec_time())
