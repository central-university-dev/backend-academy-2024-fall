import uuid
from datetime import datetime


class DomainModel:
    _oid: uuid.UUID = uuid.uuid4()
    object_name: str

    def __init__(self, object_name: str, oid: uuid.UUID = None):
        self.object_name = object_name
        if oid:
            self._oid = oid

    def __str__(self) -> str:
        return f"DomainModel str ({self._oid}, {self.object_name})"

    def __repr__(self) -> str:
        return "DomainModel(oid='{oid}', object_name='{object_name}')".format(oid=self._oid,
                                                                              object_name=self.object_name)

    def __eq__(self, other):
        return self._oid == other._oid

    def set_id(self, oid: uuid.UUID):
        self._oid = oid


class TimestampModel(DomainModel):
    current_timestamp: float

    def __init__(self, object_name: str, current_timestamp: float, oid: uuid.UUID = None):
        super().__init__(object_name, oid)
        self.current_timestamp = current_timestamp

    def __str__(self) -> str:
        return f"{self.__class__.__name__} ({self._oid}, {self.current_timestamp})"


class BarClass:
    bar: uuid.UUID

    def __init__(self, bar: uuid.UUID):
        self.bar = bar

    def __str__(self) -> str:
        return f"{self.__class__.__name__} ({self.bar})"

    def set_id(self, oid: uuid.UUID):
        self.bar = oid


if __name__ == '__main__':
    timestamp_model = TimestampModel(object_name='test', current_timestamp=datetime.now().timestamp())
    domain_model1 = DomainModel(object_name='test')
    bar_model = BarClass(bar=uuid.uuid4())

    print(timestamp_model, domain_model1, bar_model, sep='\n')

    for settable_obj in [timestamp_model, domain_model1, bar_model]:
        settable_obj.set_id(uuid.uuid4())
        print(settable_obj)
