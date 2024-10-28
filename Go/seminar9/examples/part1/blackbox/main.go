package main

import (
	"fmt"

	"backend-academy/Go/seminar9/examples/part1/blackbox/even"
)

func main() {
	const N = 8

	for i := 0; i < N; i++ {
		fmt.Printf("%d is even: %t\n", i, even.IsEven(i))
	}
}
