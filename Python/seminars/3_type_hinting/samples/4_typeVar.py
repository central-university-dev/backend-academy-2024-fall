from typing import TypeVar, List, Union

T = TypeVar('T')
T_bound = TypeVar('T_bound', bound=Union[str, int])

A_ = TypeVar('A_', bound="A")


class A:

    def __init__(self: A_, a: int):
        self.a = a


# def get_first_item(items: List[T]) -> T:
#     return items[0]

def get_first_item(items: List[T_bound]) -> T_bound:
    return items[0]


if __name__ == '__main__':
    print(type(get_first_item([1, 2, 3, 4, 5, 6])))
    print(type(get_first_item(["a", "b", "c"])))
