package failingTests_test

import (
	"slices"
	"testing"

	failingTests "backend-academy/Go/seminar9/examples/part1/failingtests"
)

func TestSumAndAppend_Errorf(t *testing.T) {
	slicesGiven := [][]int{
		{},
		{0},
		{1, 2, 3},
		{4, -5, 6, 7, -8},
	}
	slicesExpected := [][]int{
		{0},
		{0, 1},
		{1, 2, 3, 6},
		{4, -5, 6, 7, -8, 4},
	}

	for i := 0; i < len(slicesGiven); i++ {
		slice := slicesGiven[i]

		failingTests.SumAndAppend(slice)

		if !slices.Equal(slice, slicesExpected[i]) {
			t.Errorf("SumAndAppend failed, expected %v, got %v", slicesExpected[i], slice)
		}
	}
}

func TestSumAndAppend_Fatalf(t *testing.T) {
	slicesGiven := [][]int{
		{},
		{0},
		{1, 2, 3},
		{4, -5, 6, 7, -8},
	}
	slicesExpected := [][]int{
		{0},
		{0, 1},
		{1, 2, 3, 6},
		{4, -5, 6, 7, -8, 4},
	}

	for i := 0; i < len(slicesGiven); i++ {
		slice := slicesGiven[i]

		failingTests.SumAndAppend(slice)

		if !slices.Equal(slice, slicesExpected[i]) {
			t.Fatalf("SumAndAppend failed, expected %v, got %v", slicesExpected[i], slice)
		}
	}
}
