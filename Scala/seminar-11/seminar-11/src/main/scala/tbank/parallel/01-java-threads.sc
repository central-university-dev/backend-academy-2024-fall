// Несколько определений, связанных с конкурентными исполнением кода:
//
//- **многозадачность** - способность работать над несколькими задачами одновременно
//- **многопоточность** - способность программы быть исполненой на нескольких потоках или ядрах процесса
//- **конкурентность** - когда программа может исполнять несколько задач в пересекающиеся промежутки времени
//- **параллелизм** - когда программа может исполнять несколько независимых задач в параллель одновременно на нескольких ядрах
//
//**Синхронное вычисление** может вернуть либо результат, либо ошибку
//
//**Асинхронное вычисление** может вернуть либо результат, либо ошибки, либо вообще ничего

// класс, позволяющий сосдавать потоки на уровне ОС в java
import jdk.internal.misc.Unsafe

import java.lang.Thread
import java.util.concurrent.atomic.AtomicLong

// каждый поток может выполнять задачу - экземпляр интерфейса Runnable
trait MyRunnable {
  def run(): Unit
}

val t1 = new Thread(() => println(s"Hello 1"))
val t2 = new Thread(() => println(s"Hello 2"))
t1.start()
t2.start()

// системные потоки - дорогой ресурс, нужно выделять:
// - время
// - память
// - существует физический лимит в ОС на кол-во потоков

// => нужно создавать пул потоков (картинка thread-pool.png)

trait Executor {
  def execute(task: Runnable): Unit
}

val executor: Executor = ???
executor.execute(() => println(s"Hello 1"))
executor.execute(() => println(s"Hello 2"))

// в базовой реализации сервиса мы можем моделировать каждый запрос как Runnable
// отправлять его на исполнение в пул потоков
// и при исполнении отправлять результат (колбэк)

import java.net.http.{HttpRequest, HttpResponse}

trait HandleRequest {
  def handle[T](req: HttpRequest): HttpResponse[T]
}

val handler: HandleRequest = ???
def getNewRequest(): HttpRequest = ???
def sentResponse[T](resp: HttpResponse[T]): Unit = ???

while (true) {
  val req = getNewRequest()
  executor.execute { () =>
    val resp = handler.handle(req)
    sentResponse(resp)
  }
}

// Блокировка
// Потоки могут быть заблокированы, пока определенное событие не случится, которое их разблокирует
// Например, ответ от внешней системы или базы данных.
// Блокировка потока в таком случае приводит к тому, что поток не выполняет никакой работы,
// а просто ждет ответа.

// Модель памяти JVM
// Каждый поток имеет собственную стек память
// Каждый JVM процесс имеет общую heap память, которая является общей для всех потоков
// (картинка memory-in-threads.png)

// Состояние гонки
object Race1 {
  class MyCounter(var count: Int = 1)

  val counter = new MyCounter()

  val race1 = new Thread(() => counter.count += 1)
  val race2 = new Thread(() => counter.count += 1)

  race1.start()
  race2.start()
  println(counter.count) // не всегда 3 в главном потоке (картинка race-condition.png)
}

object Race2 {
  class MyCounter(@volatile var count: Int = 1)

  val counter = new MyCounter()

  val race1 = new Thread(() => counter.count += 1)
  val race2 = new Thread(() => counter.count += 1)

  race1.start()
  race2.start()
  println(counter.count) // все равно не всегда 3 в главном потоке
}

// => не избавляет от гонки все равно

object Race3 {
  class MyCounter(@volatile var count: Int = 1)

  val counter = new MyCounter()

  val t1 = new Thread(() => counter.synchronized {
    counter.count += 1
  })
  val t2 = new Thread(() => counter.synchronized { // блокирует поток => ждет, когда можно сделать запись
    counter.count += 1
  })

  t1.start()
  t2.start()
  println(counter.count) // всегда 3
}

// второй способ - неблокирующий, проще попытаться изменить переменную еще раз если не получилось
AtomicLong()
object Race4 {
  class MyAtomic(@volatile private var count: Int = 0) {
    private def compareAndSet(i: Int, i1: Int): Boolean = ???

    def update(f: Int => Int): Unit = {
      var prev = count
      while (true) {
        val updated = f(count)
        if (compareAndSet(prev, updated))
          return;
        prev = count
      }
    }
  }
}