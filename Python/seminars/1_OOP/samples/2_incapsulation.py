import uuid


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
        return "DomainModel(oid='{oid}', object_name='{object_name}')".format(oid=self._oid, object_name=self.object_name)

    def __eq__(self, other):
        return self._oid == other._oid

    def __set_name(self, new_name):
        self.object_name = new_name


if __name__ == '__main__':
    domain1 = DomainModel(object_name='test')
    domain2 = DomainModel(object_name='test', oid=domain1._oid)
    print(domain1.__repr__())
    domain_eval = eval(domain1.__repr__())
    print(domain1)

    print(domain1._DomainModel__set_name('new_test'))
    print(domain1)
