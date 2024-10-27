import dataclasses


class UserManager: # Adapter
    def get_user(self, user_id):
        # Получение пользователя из локальной базы данных
        pass


class OrderManager: # Adapter
    def create_order(self, user_id, items):
        # Создание заказа без проверки существования пользователя
        pass


@dataclasses.dataclass
class OrderService:

    user_manager: UserManager
    order_manager: OrderManager

    def create_order(self, user_id, items):
        user = self.user_manager.get_user(user_id)
        if user:
            # Создание заказа
            pass
        else:
            raise ValueError("Пользователь не найден")
