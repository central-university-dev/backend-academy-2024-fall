def process_data(data):
    result = []
    for item in data:
        if item > 0:
            result.append(item * 2)
    return result


# Использование в модуле A
def process_data_a(data):
    return process_data(data)


# Использование в модуле B
def process_data_b(data):
    return process_data(data)