class UserManager:
    def get_user(self, user_id):
        # Получение пользователя из базы данных
        pass


class OrderManager:
    def __init__(self, user_manager):
        self.user_manager = user_manager

    def create_order(self, user_id, items):
        user = self.user_manager.get_user(user_id)
        if user:
            # Создание заказа
            pass
        else:
            raise ValueError("Пользователь не найден")