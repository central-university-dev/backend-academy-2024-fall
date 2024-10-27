import logging

# Настройка логирования
logging.basicConfig(level=logging.INFO)

# Использование логгера
logger = logging.getLogger(__name__)
logger.info("Сообщение для логирования")