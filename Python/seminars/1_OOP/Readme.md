## ООП в Python
В Python ООП включает следующие основные концепции:

* **Классы**: Классы являются шаблонами или определениями для создания объектов. Они описывают состояние (переменные) и поведение (методы) объектов, которые будут созданы с их помощью.
* **Объекты**: Объекты представляют конкретные экземпляры классов. Они содержат уникальное состояние (значения переменных класса) и могут выполнять операции, определенные в классе (методы).
* **Атрибуты**: Атрибуты - это переменные, определенные внутри класса и связанные с классом или его объектами. Атрибуты могут быть переменными класса (общими для всех объектов этого класса) или переменными экземпляра (уникальными для каждого объекта).
* **Методы**: Методы - это функции, определенные внутри класса, которые определяют поведение объектов данного класса. Они могут взаимодействовать с атрибутами объекта и выполнять необходимые операции.
* **Наследование**: Наследование позволяет создавать новые классы на основе уже существующих классов, наследуя их атрибуты и методы. Подклассы могут расширять или изменять функциональность родительских классов.
* **Полиморфизм**: Полиморфизм позволяет объектам различных классов иметь одинаковые методы, но с различной реализацией. Это позволяет работать с объектами разных классов, используя общий интерфейс.
* **Инкапсуляция**: ООП обеспечивает инкапсуляцию, что означает, что данные и методы, относящиеся к объекту, объединяются в одном месте (классе). Объекты могут взаимодействовать только с публичными методами класса, скрывая внутренние детали реализации. Это упрощает управление состоянием объектов и предотвращает несанкционированный доступ к данным.

#### Плюсы ООП
* **Модульность и повторное использование кода**: ООП позволяет создавать классы, которые могут служить повторно используемыми модулями. Классы и объекты могут быть использованы в различных частях программы, что способствует повторному использованию кода и упрощает его сопровождение.
* **Улучшение разделения ответственности**: ООП помогает легко определить, какая часть кода отвечает за конкретную функциональность. Каждый объект или класс несет ответственность за свою собственную функциональность, обеспечивая более четкое разделение и легкость в понимании кода.

#### Основной минус ООП
* **Тенденция к запутанности**: Иерархии классов часто приводят к многократному переопределению методов. Само по себе это не проблема, но со временем структура может стать менее читабельной. Такие структуры тяжело поддерживать.

#### Best Practices
* **Используйте Наследование для "является" и Композицию для "имеет"**: Наследование лучше всего подходит для моделирования отношений "является", а композиция для отношений "имеет".
* **Используйте инкапсуляцию**: Скрывайте детали реализации и предоставляйте четкие интерфейсы для взаимодействия с объектами.
* **SOLID**: Стоит придерживаться принципов SOLID 

##### SOLID
* **Принцип единственной ответственности** (single responsibility principle) «Модуль должен отвечать за одного и только за одного актора.»

* **Принцип открытости/закрытости** (open-closed principle) «программные сущности … должны быть открыты для расширения, но закрыты для модификации».

* **Принцип подстановки Лисков** (Liskov substitution principle) «функции, которые используют базовый тип, должны иметь возможность использовать подтипы базового типа не зная об этом». См. также контрактное программирование.

* **Принцип разделения интерфейса** (interface segregation principle) «много интерфейсов, специально предназначенных для клиентов, лучше, чем один интерфейс общего назначения»

* **Принцип инверсии зависимостей** (dependency inversion principle) «Зависимость на Абстракциях. Нет зависимости на реализацию»