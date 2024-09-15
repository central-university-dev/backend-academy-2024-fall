import uuid
from abc import ABC, abstractmethod


class DomainModel:
    _oid: uuid.UUID = uuid.uuid4()
    object_name: str

    def __init__(self, object_name: str, oid: uuid.UUID = None):
        self.object_name = object_name
        if oid:
            self._oid = oid

    def __str__(self) -> str:
        return f"DomainModel({self._oid}, {self.object_name})"

    def __repr__(self) -> str:
        return "DomainModel(oid='{oid}', object_name='{object_name}')".format(oid=self._oid,
                                                                              object_name=self.object_name)

    def __eq__(self, other):
        return self._oid == other._oid


class Sender(ABC):

    @abstractmethod
    def send(self, obj: DomainModel) -> None:
        """Отправка данных обьекта."""


class HttpSender(Sender):
    def send(self, obj: DomainModel) -> None:
        print(f"Send model {obj} via http")


class KafkaSender(Sender):
    def send(self, obj: DomainModel) -> None:
        print(f"Send model {obj} via kafka")


if __name__ == '__main__':
    domain = DomainModel("abc", )

    HttpSender().send("domain")
    KafkaSender().send("domain")
