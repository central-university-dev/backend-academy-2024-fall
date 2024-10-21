// Тип, включающий в себя состояние всего мира
type World

// достаем из мира консоль,
// добавляем в консоль строку,
// возвращаем новый мир с новой консолью
def printLine(world: World, s: String): World = ???

// достаем из мира консоль,
// читаем строку из консоли,
// возвращаем новый мир с новой консолью и прочитанной строкой
def readLine(world: World): (World, String) = ???

def program: World => World =
  world =>
    val (world2: World, stranger: String) = readLine(world)
    val greeting: String = s"Hello, $stranger"
    val world3 = printLine(world2, greeting)
    world3

//type WorldTransformer[A] = World => (World, A)
trait WorldTransformer[A]:

  def apply(world: World): (World, A)

  // определим монадные комбинаторы
  def flatMap[B](f: A => WorldTransformer[B]): WorldTransformer[B] =
    world =>
      val (world2, state) = apply(world)
      f(state).apply(world2)

  def map[B](f: A => B): WorldTransformer[B] =
    world =>
      val (world2, state) = apply(world)
      val state2 = f(state)
      (world2, state2)

  def pure[B](b: B): WorldTransformer[B] =
    world => (world, b)

def printLineT(s: String): WorldTransformer[Unit] =
  world => (printLine(world, s), ())

def readLineT: WorldTransformer[String] =
  world => readLine(world)

def program2: WorldTransformer[Unit] =
  readLineT
    .flatMap: stranger =>
      val greeting = s"Hello, $stranger"
      printLineT(greeting)

// или через for-comprehension
def program3: WorldTransformer[Unit] =
  for
    stranger <- readLineT
    greeting: String = "Hello, $stranger"
    _ <- printLineT(greeting)
  yield ()

// альтернативная точка входа в программу, с нашим типом
// это конец/граница (чистого) мира
def run: WorldTransformer[Unit] = program3

// граница между чистыми функциями и нечистым рантаймом
def main(): Unit =
  // как-то вызываем run, все сайд-эффекты вычисляются тут
  ???


// каждый раз, когда функция возвращает WorldTransformer,
// мы знаем, что будет производиться какой-то сайд-эффект.
//
// таким образом, мы явно указываем использование сайд-эффектов в программе,
// оставляя чистоту и ссылочную прозрачность.
//
// при этом сайд-эффекты могут содержать всё, что угодно:
// работа с консолью, общение с драйвером базы данных, взаимодействие по сети.
//
// суть в том, что мы должны выделять всю нашу логику в функции без эффектов,
// чтобы поддерживать local reasoning и тестируемость

// такой подход к работе с сайд-эффектами называют IO монадой
type IO[A] = WorldTransformer[A]
