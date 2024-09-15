import builtins


class OverProcessor:
    def process(self, value: int | str) -> str:
        match type(value):
            case builtins.str:
                return f"Changed str {value}"
            case builtins.int:
                return f"Changed int {value}"
            case _:
                raise TypeError(f"Unexpected type {type(value)}")


if __name__ == '__main__':
    print(OverProcessor().process("50"))
    print(OverProcessor().process(120))
    print(OverProcessor().process(object()))
