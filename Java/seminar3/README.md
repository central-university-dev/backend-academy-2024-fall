## Вступление
* Обзор парадигм, которые уже прошли (ФП + принципы ООП)
* Давайте вернемся к ООП в Java и посмотрим, как сделать его приятнее и безопаснее в использовании благодаря новым версиям языка. А тема сегодняшнего урока поможет писать на ФП.

## Sealed types
* И так начнем. Бывали ли ситуации, когда в какой-то класс не должен иметь наследников?
* Как сделать класс таким, чтобы от него нельзя было наследоваться? - `final` modifier
* Например, вы пишите библиотеку и не хотите давать возможность некоторым классам иметь наследников. Мы не хотим давать пользователям ружье, которым они выстрелят себе в ногу. Это не будет ломать public api вашей библиотеки:
  ```java
  public class File {  
      private String name;  
      private String path;  
    
      private boolean exists()                                { return true; }  
      private byte[] read()                                   { return new byte[0]; }  
      private void write(byte[] data)                         { }  
  }  
    
  class MyFile extends File {  
      public void create() {  
          if (System.getProperty("os.name").equals("Windows")) {  
              // Implementation of creation  
          } else {  
              throw new UnsupportedOperationException("OS is not Windows");  
          }  
      }  
  }
  ```
* Другой пример: у вас есть классы, объекты которых должны быть иммутабельны по своей концепции.
  ```java
  public final class String { }
  ```
  Добавить статью: почему String иммутабельный - https://www.baeldung.com/java-string-immutable
* Еще пример: вспомните принцип prefer composition over inheritance: это поможет тоже
  Пицца
* Окей, здорово! А теперь давайте подумаем, что если нам нужно, чтобы у класса было ограниченное число состояний или наследников? Например, класс
  Hero
* Переходим к реализации `Hero` через sealed class-ы
* Что за non-sealed ?
  https://stackoverflow.com/questions/63860110/what-is-the-point-of-extending-a-sealed-class-with-a-non-sealed-class
* Pattern matching
  Если класс не sealed, приходится писать дефолт
  А сейчас не нужно
* Какие ошибки sealed типы могут помочь предотвратить?
*
## Records
* Что такое POJO / DTO? Привести пример и для чего используется. Так мы писали раньше
* А теперь внимание! Рекорды! Можно реализовать те же DTO с помощью них)
- Зачем нам нужны рекорды? Почему не использовать обычные объекты?
  Меньше boilerplate кода, безопаснее и проще
- Зачем в рекорде кастомные конструкторы? - Пример Range: конструктор только с end (canonical constructor call), deep copying (что-то со списком), валидация (start < end и затем компактный конструктор)
- Оффтоп: не злоупотребляйте валидацией в конструкторе
- Как и говорили рекорды нельзя сделать наследуемыми, но можно реализовывать интерфейсы => Iterable
- Уязвимость serialazable (при сериализации конструктор класса не вызывается), а т.к. canonical constructor должен быть вызван обязательно, в рекордах этот момент пофикшен. Даже если вы не пользуетесь сериализацией, другие фреймворки делают это!
- Pattern matching with records.
  Реализуем фигуры с помощью интерфейса Shape (в презе): rectange, circle. Затем добавляем новую фигуру, все отлично (Open/Close). Но пришел заказчик и говорит, хочу считать диаметр фигур, и придется нарушать O/C принцип. Представим, что диаметр нужен не для всех фигур, че делать? - Можем сделать сегрегацию интерфейсов, чтобы еще соблюдать и Interface Segregation (омайгарабл), тогда будем меньше нарушать O/C, но все равно может быть неприятно, если требования будут приходить часто и много.

  Давайте делать по-другому:
  ```java
  
  if (shape instanceof Circle) {
      var circle = (Circle) shape;
      return Math.PI * circle.radius * circle.radius();
  } else if (shape instanceof Rectange) {
      var rectangle = (Circle) shape;
      return square.height() * square.width();
  } else {
      ?
  }
  ```
  Уродливо, да?
  ```java
  // area
  if (shape instanceof Circle circle) {
      return Math.PI * circle.radius * circle.radius();
  } else if (shape instanceof Rectange rectangle) {
      return square.height() * square.width();
  } else {
      ?
  }
  ```
  Сила паттерн матчинга! (Определение pattern mathcing)
  Обращаю внимание, что мы все еще поддерживаем кейс, что сюда можно передать параллелограмм, что тоже не очень хорошо, но этот метод можем иметь подобный контракт, а если такой контракт будет в методе самого класса, это будет странно.

  Давайте делать лучше!
  ```java
  return switch (shape) {
      case Circle circle -> Math.PI * circle.radius * circle.radius();
      case Rectange rectangle -> rectangle.height() * rectangle.width();
      default -> throw new IllegalArgumentException();
  }
  ```
  Еще лучше! Deconstruction (21 версия)
  ```java
  return switch (shape) {
      case Circle(double radius) -> Math.PI * circle.radius * circle.radius();
      case Rectange(double height, double width) -> rectangle.height() * rectangle.width();
      default -> throw new IllegalArgumentException();
  }
  ```
  Можем ли сделать код еще красивее и безопаснее? - Да, с помощью sealed.
  ```java
  public sealed interface Shape
  permits Circle, Rectangle { }
  ...
  return switch (shape) {
      case Circle circle -> Math.PI * circle.radius * circle.radius();
      case Rectangle rectangle -> rectangle.height() * rectangle.width();
      // default -> throw new IllegalArgumentException(); показать warning в идее, строчку можно удалить
  }
  ```

- ? Какие задачи рекорды не решают? Пример: комплексные числа. Лучше бы их реализовать value type из проекта valhalla
### Практика
- Давайте попрактикуемся? Нужно сделать программку по следующему ТЗ. Затем показываю, как бы это выглядело на старой Java и как может выглядеть сейчас.
