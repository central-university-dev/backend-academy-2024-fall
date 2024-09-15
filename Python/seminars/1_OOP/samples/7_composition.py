from abc import ABC, abstractmethod


class Shape(ABC):

    @abstractmethod
    def area(self) -> float:
        """Площадь фигуры"""


class Circle(Shape):

    def __init__(self, radius: float):
        self._radius = radius

    def area(self) -> float:
        return 3.14 * (self._radius ** 2)


class Rectangle(Shape):

    def __init__(self, width: float, height: float):
        self._width = width
        self._height = height

    def area(self) -> float:
        return self._width * self._height


class Address:
    def __init__(self, name: str, address: str):
        self.name = name
        self._address = address

    def get_address(self):
        return self._address


class Person:

    def __init__(self, name: str, address: Address):
        self.name = name
        self.address = address

    def get_address(self):
        return self.get_address()





