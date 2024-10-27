# Модуль A
import module_b


def function_a():
    # Использует функции из module_b и module_c
    module_b.function_b()
    module_c.function_c()


# Модуль B
import module_a


def function_b():
    # Использует функции из module_a
    module_a.function_a()


# Модуль C
import module_a
import module_b


def function_c():
    # Использует функции из module_a и module_b
    module_a.function_a()
    module_b.function_b()