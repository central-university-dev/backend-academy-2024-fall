class User: # God-object, spaghetti
    def __init__(self, name, age):
        self.name = name
        self.age = age


class DbService:
    def __init__(self):
        self.db_connection = DatabaseConnection()

    def save(self, user: User):
        # Сохранение пользователя в базе данных
        self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({user.name}, {user.age})")


class Email:
    def __init__(self):
        self.email_service = EmailService()

    def send_email(self, user: User, message):
        self.email_service.send(user.name, message)


class Adapter:
    def run(self):
        user1 = User('name', 'age')
        db = DbService()
        db.save(user1)
        mail = Email
        mail.send_email(user1, 'message')
