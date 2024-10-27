from typing import Callable


def greet(name: str) -> Callable:
    def inner() -> str:
        return f"Hello {name}"

    return inner


class Adder:

    def __init__(self, value: int) -> None:
        self.value = value

    def __call__(self, x: int) -> int:
        return self.value + x


if __name__ == '__main__':
    greet_func: Callable = greet(name="John")
    print(greet_func())

    add_func: Adder = Adder(2)
    print(add_func(3))
