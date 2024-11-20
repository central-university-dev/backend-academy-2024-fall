package main

import (
	"fmt"
	"sync"
	"sync/atomic"
)

func MakeSlice(N int) []int {
	result := make([]int, 0, N)
	for i := 0; i < N; i++ {
		result = append(result, i)
	}

	return result
}

type NumberChecker func(int) bool

func NumberCheckerV1(number int) bool {
	return number%2_000 == 0
}

func NumberCheckerV2(number int) bool {
	return number%200 == 0
}

func NumberCheckerV3(number int) bool {
	return number%20 == 0
}

type SeqCounter struct {
	srcData       []int
	numberChecker NumberChecker
}

func NewSeqCounter(srcData []int, numberChecker NumberChecker) SeqCounter {
	return SeqCounter{
		srcData:       srcData,
		numberChecker: numberChecker,
	}
}

func (sc SeqCounter) CountCheckedNumbers() int {
	result := 0
	for _, number := range sc.srcData {
		if sc.numberChecker(number) {
			result++
		}
	}

	return result
}

type WorkerPoolWithMutexCounter struct {
	buckets       [][]int
	workersQty    int
	numberChecker NumberChecker
}

func NewWorkerPoolWithMutexCounter(srcData []int, workersQty int, numberChecker NumberChecker) WorkerPoolWithMutexCounter {
	buckets := make([][]int, workersQty)
	for i, number := range srcData {
		bucketIdx := i % workersQty
		buckets[bucketIdx] = append(buckets[bucketIdx], number)
	}

	return WorkerPoolWithMutexCounter{
		buckets:       buckets,
		workersQty:    workersQty,
		numberChecker: numberChecker,
	}
}

func (wpc WorkerPoolWithMutexCounter) CountCheckedNumbers() int {
	result := 0

	mu := sync.Mutex{}
	wg := sync.WaitGroup{}

	for i := 0; i < wpc.workersQty; i++ {
		wg.Add(1)
		go func(workerId int) {
			defer wg.Done()

			bucket := wpc.buckets[workerId]

			for _, number := range bucket {
				if wpc.numberChecker(number) {
					mu.Lock()
					result++
					mu.Unlock()
				}
			}
		}(i)
	}
	wg.Wait()

	return result
}

type WorkerPoolWithoutMutexCounter struct {
	buckets       [][]int
	workersQty    int
	numberChecker NumberChecker
}

func NewWorkerPoolWithoutMutexCounter(srcData []int, workersQty int, numberChecker NumberChecker) WorkerPoolWithoutMutexCounter {
	buckets := make([][]int, workersQty)
	for i, number := range srcData {
		bucketIdx := i % workersQty
		buckets[bucketIdx] = append(buckets[bucketIdx], number)
	}

	return WorkerPoolWithoutMutexCounter{
		buckets:       buckets,
		workersQty:    workersQty,
		numberChecker: numberChecker,
	}
}

func (wpc WorkerPoolWithoutMutexCounter) CountCheckedNumbers() int {
	wg := sync.WaitGroup{}
	partialResults := make([]int, wpc.workersQty)

	for i := 0; i < wpc.workersQty; i++ {
		wg.Add(1)
		go func(workerId int, partialResult *int) {
			defer wg.Done()

			bucket := wpc.buckets[workerId]

			for _, number := range bucket {
				if wpc.numberChecker(number) {
					*partialResult++
				}
			}
		}(i, &partialResults[i])
	}
	wg.Wait()

	result := 0
	for _, partialResult := range partialResults {
		result += partialResult
	}

	return result
}

type WorkerPoolAtomicCounter struct {
	buckets       [][]int
	workersQty    int
	numberChecker NumberChecker
}

func NewWorkerPoolAtomicCounter(srcData []int, workersQty int, numberChecker NumberChecker) WorkerPoolAtomicCounter {
	buckets := make([][]int, workersQty)
	for i, number := range srcData {
		bucketIdx := i % workersQty
		buckets[bucketIdx] = append(buckets[bucketIdx], number)
	}

	return WorkerPoolAtomicCounter{
		buckets:       buckets,
		workersQty:    workersQty,
		numberChecker: numberChecker,
	}
}

func (wpc WorkerPoolAtomicCounter) CountCheckedNumbers() int {
	result := atomic.Int64{}
	wg := sync.WaitGroup{}

	for i := 0; i < wpc.workersQty; i++ {
		wg.Add(1)
		go func(workerId int) {
			defer wg.Done()

			bucket := wpc.buckets[workerId]

			for _, number := range bucket {
				if wpc.numberChecker(number) {
					result.Add(1)
				}
			}
		}(i)
	}
	wg.Wait()

	return int(result.Load())
}

func main() {
	const (
		N          = 2_000_000
		workersQty = 7
	)

	srcData := MakeSlice(N)

	fmt.Printf("counter 1: %d\n", NewSeqCounter(srcData, NumberCheckerV1).CountCheckedNumbers())
	fmt.Printf("counter 2: %d\n", NewWorkerPoolWithMutexCounter(srcData, workersQty, NumberCheckerV1).CountCheckedNumbers())
	fmt.Printf("counter 3: %d\n", NewWorkerPoolWithoutMutexCounter(srcData, workersQty, NumberCheckerV1).CountCheckedNumbers())
	fmt.Printf("counter 4: %d\n", NewWorkerPoolAtomicCounter(srcData, workersQty, NumberCheckerV1).CountCheckedNumbers())
}
