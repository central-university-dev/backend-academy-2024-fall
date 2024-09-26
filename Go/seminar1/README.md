# Структуры в Go

- [Структуры в Go](#структуры-в-go)
  - [Типы данных](#типы-данных)
  - [Структуры](#структуры)
  - [Встраивание структур](#встраивание-структур)
  - [Композиция структур](#композиция-структур)
  - [Методы структур](#методы-структур)

## Типы данных

Добавить:
- Поинтеры в целом и ресиверя по поинтеру/по значению
- Корнер кейсы(множественное встраивание, и тд)

В Go есть несколько типов данных:

- Скалярные типы данных
  - Булевый тип `bool` -> `var a bool = true` или `var a bool = false`
  - Строковый тип `string` -> `var a string = "Академия Бэкенда"`
  - Целочисленные типы
    - Знаковые
      - `int` -> `var a int = -1`
      - `int8` -> `var a int8 = -1`
      - `int16` -> `var a int16 = -1`
      - `int32` -> `var a int32 = -1`
      - `int64` -> `var a int64 = -1`
    - Беззнаковые
      - `uint` -> `var a uint = 1`
      - `uint8` -> `var a uint8 = 1`
      - `uint16` -> `var a uint16 = 1`
      - `uint32` -> `var a uint32 = 1`
      - `uint64` -> `var a uint64 = 1`
    - `byte` == `uint8`
    - `rune` == `int32` -> `var a rune = 'А'`
  - Числа с плавающей запятой
    - `float32` -> `var a float32 = 1.1`
    - `float64` -> `var a float64 = 1.1`
  - Комплексные числа
    - `complex64` -> `var a complex64 = 1.1 + 1.1i`
    - `complex128` -> `var a complex128 = 1.1 + 1.1i`
- Коллекции
  - Массив -> `var a [3]int = [3]int{1, 2, 3}`
  - Срез -> `var a []int = []int{1, 2, 3}`
  - Словарь -> `var a map[string]int = map[string]int{"one": 1, "two": 2}`

## Структуры

Структуры используются для описания сущностей, которые имеют некое конечное количество характеристик.

```go
type Person struct {
  Name string
  Age int
}
```

С полями структур можно взаимодействовать через оператор `.`

```go
var person = Person {
  Name: "Bob",
  Age: 20,
}

fmt.Println(person.Name) // Bob
fmt.Println(person.Age) // 20

person.Age = 21

fmt.Println(person.Age) // 21
```

## Встраивание структур

```go
type User struct {
  Person
  Email string
}

var user = User{
  Person: person,
  Email:  "bob@example.com",
}

fmt.Println(user.Name)  // Bob
fmt.Println(user.Email) // bob@example.com
```

В одну структуру может быть встроено несколько структур

```go
type Employee struct {
  JobTitle string
}

type User struct {
  Person
  Employee
  Email string
}
```

## Композиция структур

Структуры могут быть вложены друг в друга.

```go
type Course struct {
  Name string
}

type Student struct {
  User User
  Course struct {
    Name string
  }
}

var student = Student{
  User: user,
  Course: Course{
    Name: "Go Backend Academy",
  }
}

fmt.Println(student.User.Name) // Bob
fmt.Println(student.Course.Name) // Go Backend Academy
```

## Методы структур

```go
func (u User) String() string{
  return fmt.Sprintf("User Name: %s, Email: %s", u.Name, u.Email)
}

fmt.Println(user.String()) // User Name: Bob, Email: bob@example.com
fmt.Println(User.String(user)) // User Name: Bob, Email: bob@example.com
```

Тип, для которого определен метод, называется ресивером. Ресивер может быть двух видов: по значению и по ссылке. Метод с ресивером по значению получает копию объекта и не может изменять исходный объект.


Методы у структур с встроенной структурой могут ходить по цепочке вложенности.

```go
func (u Person) Say() string {
	return "Hello"
}

fmt.Println(user.Say()) 
```