package codeThatPanics

func FuncThatPanics(x int) {
	if x%2 == 0 {
		panic("omg! x is even!!!")
	}
}
