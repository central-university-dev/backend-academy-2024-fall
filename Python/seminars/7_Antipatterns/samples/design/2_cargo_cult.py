# Использование сложного паттерна Singleton без необходимости

class Singleton:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(Singleton, cls).__new__(cls)
        return cls._instance


class Logger(Singleton):
    def log(self, message):
        print(message)


if __name__ == '__main__':

    logger = Logger()

    logger_2 = Logger()

    print(logger._instance)
    print(logger_2._instance)

