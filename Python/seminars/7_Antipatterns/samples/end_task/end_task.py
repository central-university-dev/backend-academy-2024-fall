# Найдите антипаттерны

class User:
    def __init__(self, name, age):
        self.name = name
        self.age = age
        self.db_connection = DatabaseConnection()

    def save(self):
        # Сохранение пользователя в базе данных
        self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({self.name}, {self.age})")

    def send_email(self, message):
        # Отправка email пользователю
        email_service = EmailService()
        email_service.send(self.name, message)