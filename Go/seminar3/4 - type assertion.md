## Type assertion

В golang есть возможность привести значение интерфейсного типа к другому статическому типу.
Тот тип, к которому приводят значение, тоже может быть интерфейсным.

Поддерживается 2 формы записи для такой инструкции (на примере приведения к типу string):
```go
x, ok := v.(string) // 1 форма
x := v.(string) // 2 форма
```
2 форма вызовет панику, если преобразование типа окажется неудачным.

Type assertion выполняется с помощью значение iface.data и iface.tab._type.

Пример. Функция принимает значение типа any (то же, что и interface{}) и делает попытку преобразования в string.
Дочерний тип MyString также не проходит проверку, т.е. при type assertion проверяется конкретный фактический тип.
```go
package main

import "fmt"

func acceptsInterface(value any) {
	str, ok := value.(string)
	if ok {
		fmt.Println("the value is a string: ", str)
	} else {
		fmt.Println("the value is not a string")
	}
}

type MyString string

func main() {
	acceptsInterface("hello") // the value is a string:  hello
	acceptsInterface(MyString("hello")) // the value is not a string
	acceptsInterface(123) // the value is not a string
}
```