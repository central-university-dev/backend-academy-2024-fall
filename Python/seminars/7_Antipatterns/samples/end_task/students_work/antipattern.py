class User:
    # До этого была жёсткая связь user с databaseconnection и emailservice
    def __init__(self, name, age):
        self.name = name
        self.age = age


class UserRepository:
    def __init__(self, db_connection):
        self.db_connection = db_connection

    def save(self, user):
        try:
            self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({user.name}, {user.age})")
        except Exception as e:
            pass


class EmailService:
    def send(self, to, message):
        pass


class UserService:
    def __init__(self, user_repository, email_service):
        self.user_repository = user_repository
        self.email_service = email_service

    def register_user(self, name, age, welcome_message):
        user = User(name, age)
        self.user_repository.save(user)
        self.email_service.send(user.name, welcome_message)


db_connection = DatabaseConnection()
user_repository = UserRepository(db_connection)
email_service = EmailService()
user_service = UserService(user_repository, email_service)
user_service.register_user("Bob", 25, "Success")
