from typing import Protocol
from abc import abstractmethod
# Найдите антипаттерны


class User:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def get_name(self) -> str:
        return self.name

    def get_age(self) -> int:
        return self.age


class Saver(Protocol):

    @abstractmethod
    def save(self, user: User):
        pass


class SaveToDB(Saver):
    def __init__(self):
        self.db_connection = DatabaseConnection()

    def save(self, user: User):
        self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({User.get_name()}, {User.get_age()})")


class Sender(Protocol):

    @abstractmethod
    def send(self, user: User, message):
        pass


class EmailSender(Sender):

    def __init__(self):
        self.email_service = EmailService()

    def send(self, user: User, message):
        self.email_service.send(user.get_name(), message)
