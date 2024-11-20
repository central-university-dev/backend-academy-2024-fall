package main

import (
	"testing"
)

const (
	SrcDataSize = 2_000_000
	WorkersQty  = 7
)

func BenchmarkSeqCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewSeqCounter(srcData, NumberCheckerV1)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}

func BenchmarkWorkerPoolCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewWorkerPoolCounter(srcData, WorkersQty, NumberCheckerV1)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}

func BenchmarkWorkerPoolWithoutMutexCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewWorkerPoolWithoutMutexCounter(srcData, WorkersQty, NumberCheckerV1)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}
