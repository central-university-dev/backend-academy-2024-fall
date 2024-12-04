package main

import (
	"fmt"
	"sync"
)

func BufferedRangeGenerator(rangeStart, rangeEnd, bufferSize int) chan int {
	result := make(chan int, bufferSize)

	go func() {
		defer close(result)

		for i := rangeStart; i <= rangeEnd; i++ {
			result <- i
		}
	}()

	return result
}

func FanIn[T any](sources ...<-chan T) <-chan T {
	combined := make(chan T)

	wg := sync.WaitGroup{}
	wg.Add(len(sources))

	for _, source := range sources {
		go func(src <-chan T) {
			defer wg.Done()

			for data := range src {
				combined <- data
			}
		}(source)
	}

	go func() {
		wg.Wait()
		close(combined)
	}()

	return combined
}

func main() {
	combined := FanIn(
		BufferedRangeGenerator(1, 3, 1),
		BufferedRangeGenerator(4, 7, 1),
		BufferedRangeGenerator(8, 10, 1),
	)

	for data := range combined {
		fmt.Printf("received number %d from combined chan\n", data)
	}
}
