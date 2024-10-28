package main

import "fmt"

func privateIsEven(x int) bool {
	return x%2 == 0
}

func main() {
	const N = 8

	for i := 0; i < N; i++ {
		fmt.Printf("%d is even: %t\n", i, privateIsEven(i))
	}
}
