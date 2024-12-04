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

func WorkerFn(ownId int, source chan int, wg *sync.WaitGroup) {
	defer wg.Done()

	for number := range source {
		fmt.Printf("I'm worker #%d, processing number %d\n", ownId, number)
	}
}

func main() {
	const (
		rangeStart = 1
		rangeEnd   = 8
		poolSize   = 3
	)

	src := BufferedRangeGenerator(rangeStart, rangeEnd, poolSize)

	wg := sync.WaitGroup{}
	for i := 0; i < poolSize; i++ {
		wg.Add(1)
		go WorkerFn(i, src, &wg)
	}
	wg.Wait()
}
