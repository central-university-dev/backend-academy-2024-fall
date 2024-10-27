class Application:
    def __init__(self):
        self.database = Database()
        self.ui = UserInterface()
        self.logic = BusinessLogic()
        self.logic_2 = BusinessLogic2()

    def run(self):
        data = self.database.get_data()
        processed_data = self.logic.process_data(data)
        self.ui.display(processed_data)