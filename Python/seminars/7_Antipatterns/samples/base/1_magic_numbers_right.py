# Размер скидки в процентах
DISCOUNT_RATE = 15


def calculate_discount(price):
    return price * (1 - DISCOUNT_RATE / 100)
