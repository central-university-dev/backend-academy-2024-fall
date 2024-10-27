class DataProcessor:
    def process(self, data):
        for item in data:
            handler = self.get_handler(item['type'])
            handler.process(item)

    def get_handler(self, item_type):
        handlers = {
            'A': TypeAHandler(),
            'B': TypeBHandler(),
        }
        return handlers.get(item_type, DefaultHandler())


class TypeAHandler:
    def process(self, item):
        # Обработка типа A
        pass


class TypeBHandler:
    def process(self, item):
        # Обработка типа B
        pass


class DefaultHandler:
    def process(self, item):
        # Обработка остальных типов
        pass


# Использование
processor = DataProcessor()
processor.process(data)