# Исправлено нарушение SRP в классе User (ответственность за создание пользователя, базу данных и отправку)
# Отсюда вытекал GodObject, по крайней мере здесь, они явно не на одном уровне абстракции:
# self.name = name
# self.age = age
# self.db_connection = DatabaseConnection()


from dataclasses import dataclass


@dataclass
class User:
    name: str
    age: int


@dataclass
class DataBaseAdapter:

    def execute(self, user: User) -> None:
        pass


@dataclass
class EmailSender:

    # В контексте этой задаче вовсе staticmethod может быть
    def send_email(self, id: User, message: str) -> str:
        return f"{message} was sent to {id}"


#################


class User:
    def __init__(self, name, age, db_connection, email_service):
        self.name = name
        self.age = age
        # уменьшаем зависимость от конкретной реализации сервисов (god object)
        self.db_connection = db_connection
        self.email_service = email_service

    def save(self):
        # Сохранение пользователя в базе данных
        # sink abstraction?
        try:
            self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({self.name}, {self.age})")
        except Exception as e:
            print('error save ', e)

    def send_email(self, message):
        # Отправка email пользователю
        # sink abstraction?
        try:
            self.email_service.send(self.name, message)
        except Exception as e:
            print('error send ', e)
