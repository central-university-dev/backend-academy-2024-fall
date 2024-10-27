def process_data(data):
    for item in data:
        if item['type'] == 'A':
            # Обработка типа A
            process_type_a(item)
        elif item['type'] == 'B':
            # Обработка типа B
            process_type_b(item)
        else:
            # Обработка остальных типов
            process_other_types(item)
    # Дополнительная логика
    if some_condition(data):
        perform_additional_processing(data)
    # Еще больше условий и логики
    # ...

