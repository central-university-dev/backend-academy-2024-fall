     
class UserManager:
    def __init__(self, db_connection, email_service):
        self.db_connection = db_connection
        self.email_service = email_service
        
    def save(self, user):
        self.db_connection.execute(f"INSERT INTO users (name, age) VALUES ({self.name}, {self.age})")

    def send_email(self, message):
        self.email_service.send(self.name, message)
        
class User:
    def __init__(self, name, age):
        self.name = name
        self.age = age


    