enum Taste:
  case Sweet, Sour, Salty, Bitter, Umami

class Food(val name: String, val taste: Taste)

val doshirak = new Food("Doshirak", Taste.Umami)
val sweetCandy = new Food("Alyonka", Taste.Sweet)
val sourCandy = new Food("Кислинка", Taste.Sour)

if doshirak.taste == Taste.Umami then println("Truly soy meet taste...")

enum Color(rgb: Int):
  case Red extends Color(0xff0000)
  case Green extends Color(0x00ff00)
  case Blue extends Color(0x0000ff)

enum Planet(mass: Double, radius: Double):
  private final val G = 6.67300e-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) =
    otherMass * surfaceGravity

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Earth extends Planet(5.976e+24, 6.37814e6)


// Мини-задачки на самопроверку

// 1. Создайте enum с названием `DayOfWeek` и значениями `Monday`, `Tuesday`, `Wednesday`, `Thursday`, `Friday`, `Saturday`, `Sunday`.
// 2. Добавьте 2 метода в enum: `isWeekend` и `isWeekday`.
// 3. Напишите примеры использования enum.