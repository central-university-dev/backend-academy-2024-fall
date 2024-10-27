class FileReader:
    def read(self, file_path):
        try:
            with open(file_path, 'r') as f:
                return f.read()
        except Exception as e:
            return e  # Возвращаем исключение пользователю