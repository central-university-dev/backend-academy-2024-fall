class User:
    """
    Антипаттерны:
    1. Жесткое внедрение зависимостей
    2. Нарушение принципа единственной ответственности
    3. SQL-инъекции
    4. Отсутствие валидации данных
    5. Необрабатываемые ошибки
    """

    def __init__(self, name, age, db_connection, email_service):
        if not name or not isinstance(name, str):
            raise ValueError("Invalid name")
        if not isinstance(age, int) or age < 0:
            raise ValueError("Invalid age")

        self.name = name
        self.age = age
        self.db_connection = db_connection
        self.email_service = email_service

    def save(self):
        query = "INSERT INTO users (name, age) VALUES (?, ?)"
        self.db_connection.execute(query, (self.name, self.age))

    def send_email(self, message):
        try:
            self.email_service.send(self.name, message)
        except Exception as e:
            print(f"Error sending email: {e}")
            raise