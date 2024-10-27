from typing import Union, List, Dict, Tuple, Optional, Callable, TypeVar

T = TypeVar('T', bound=Union[str, int])


def foo(a: int, b: int) -> float:  # pylint: disable=missing-function-docstring, disallowed-name
    return a / b


def bar(a, b, c):
    return str(a) + str(b) + str(c)


def foo_list(a_list: List[str]) -> str:
    return ", ".join(a_list)


def bar_dict(b_dict: Dict[str, int], filter_func: Callable[[int], bool]) -> List[int]:
    return [v for k, v in b_dict.items() if filter_func(v)]


def foo_tuple(
        a_tuple: Tuple[
            Optional[Union[str, int]], Optional[Union[str, int]], Optional[Union[str, int]]
        ]
) -> List[Optional[Union[str, int]]]:
    return list(a_tuple)


if __name__ == "__main__":
    print(foo(1, 2))
    print(bar(1, 2, 3))
    print(bar("1", "2", 3))
    print(foo_list(["1", "3", 4]))
    print(bar_dict({"a": 1, "b": 2, "c": 3}, lambda x: x % 2 == 0))
    print(foo_tuple(("a", "b", "c")))
