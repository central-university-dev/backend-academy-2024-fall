import uuid


class DomainModel:
    oid: uuid.UUID = uuid.uuid4()
    object_name: str

    def __init__(self, object_name: str, oid: uuid.UUID = None):
        self.object_name = object_name
        if oid:
            self.oid = oid

    def __str__(self) -> str:
        return f"DomainModel str ({self.oid}, {self.object_name})"

    def __repr__(self) -> str:
        return "DomainModel(oid='{oid}', object_name='{object_name}')".format(oid=self.oid, object_name=self.object_name)

    def __eq__(self, other):
        return self.oid == other._oid


if __name__ == '__main__':
    domain1 = DomainModel(object_name='test')
    domain2 = DomainModel(object_name='test', oid=domain1.oid)
    domain_eval = eval(domain1.__repr__())
    print(domain_eval._oid)
