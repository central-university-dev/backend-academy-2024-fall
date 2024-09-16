## Type switch

Есть особая форма оператора switch, в которой можно делать переключение в зависимости от типа переменной.
Хороший пример есть в [go tour](https://go.dev/tour/methods/16):

```go
package main

import "fmt"

func do(i any) {
	switch v := i.(type) {
	case int:
		fmt.Printf("Twice %v is %v\n", v, v*2)
	case string:
		fmt.Printf("%q is %v bytes long\n", v, len(v))
	default:
		fmt.Printf("I don't know about type %T!\n", v)
	}
}

func main() {
	do(21)
	do("hello")
	do(true)
}
```

Стоит добавить, что сравнение с типами из case реализуется с помощью всё того же iface.tab._type.
