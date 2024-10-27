# God Object: Класс User выполняет слишком много обязанностей.
# Протекающая абстракция: Использование деталей реализации внутри класса User.
# Отсутствие инъекции зависимостей: Прямое создание DatabaseConnection и EmailService внутри метода.
from unittest.mock import MagicMock


class User:
    def __init__(self, name, age):
        self.name = name
        self.age = age


class UserRepository:
    def __init__(self, db_connection):
        self.db_connection = db_connection

    def save(self, user):
        self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({user.name}, {user.age})")


class EmailNotifier:
    def __init__(self, email_sender):
        self.email_sender = email_sender

    def send_email(self, user, message):
        self.email_sender.send(user.name, message)


if __name__ == '__main__':
    # Использование
    db_connection = MagicMock()
    user_repo = UserRepository(db_connection)
    email_service = MagicMock()
    notifier = EmailNotifier(email_service)

    user = User("Alice", 30)
    user_repo.save(user)
    notifier.send_email(user, "Welcome!")
