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
		counter := NewSeqCounter(srcData, NumberCheckerV3)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}

func BenchmarkWorkerPoolWithMutexCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewWorkerPoolWithMutexCounter(srcData, WorkersQty, NumberCheckerV3)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}

func BenchmarkWorkerPoolWithoutMutexCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewWorkerPoolWithoutMutexCounter(srcData, WorkersQty, NumberCheckerV3)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}

func BenchmarkWorkerPoolAtomicCounter(b *testing.B) {
	srcData := MakeSlice(SrcDataSize)
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		b.StopTimer()
		counter := NewWorkerPoolAtomicCounter(srcData, WorkersQty, NumberCheckerV3)
		b.StartTimer()

		counter.CountCheckedNumbers()
	}
}
