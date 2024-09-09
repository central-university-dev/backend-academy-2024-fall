// if

var condition: Boolean = true

if (condition) {
  println("Condition is true")
  condition = false
} else {
  println("Condition if false")
  condition = true
}

if (condition)
  println("Condition is true")
else {
  println("Condition if false")
  condition = true
}

// `if` можно писать в одну строчку
// Аналог синтаксиса `?` `:` из Java
val result = if (condition) "yes" else "no"
println(result)

// else if
val conditionOne: Boolean = ???
val conditionTwo: Boolean = ???

val cases =
  if (conditionOne)
    "case 1"
  else if (conditionTwo)
    "case 2"
  else
    "case 3"

// while

var index: Int = 0

while (index < 5) {
  println(index)
  index += 1
}

// for

for (idx <- 1 to 5) {
  println(idx)
}



// Мини-задачки на самопроверку

// Напишите программу, которая считает сумму всех четных чисел в диапазоне от 1 до 10, которые делятся на 3 или на 5.
//  1. С использованием while
//  2. С использованием for

