from typing import Literal, Final


def process_status(status: Literal["start", "stop"]) -> None:
    if status == "start":
        print("Processing started")
    elif status == "stop":
        print("Processing stopped")
    else:
        print("Invalid status")


pi: Final = 3.14


class BaseClass:
    def __init__(self):
        self._value: Final = 10


class DerivedClass(BaseClass):
    def __init__(self):
        super().__init__()
        self._value: Final = 20


if __name__ == '__main__':
    process_status("start")
    process_status("unknown")

    pi = 5

    obj = DerivedClass()
