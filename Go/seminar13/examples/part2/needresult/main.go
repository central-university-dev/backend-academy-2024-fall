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

func WorkerFn(ownId int, source chan int, res chan string, wg *sync.WaitGroup) {
	defer wg.Done()

	for number := range source {
		res <- fmt.Sprintf("I'm worker #%d, processing number %d", ownId, number)
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
	res := make(chan string, poolSize)
	for i := 0; i < poolSize; i++ {
		wg.Add(1)
		go WorkerFn(i, src, res, &wg)
	}

	go func() {
		wg.Wait()
		close(res)
	}()

	for out := range res {
		fmt.Println(out)
	}
}
