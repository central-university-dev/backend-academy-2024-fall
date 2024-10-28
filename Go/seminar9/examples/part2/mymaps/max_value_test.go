package myMaps_test

import (
	"fmt"
	"math"
	"testing"

	"github.com/stretchr/testify/assert"

	myMaps "backend-academy/Go/seminar9/examples/part2/mymaps"
)

func TestMaxValue(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		dict     map[string]float64
		expected float64
	}

	testCases := []TestCase{
		{
			dict:     nil,
			expected: math.Inf(-1),
		},
		{
			dict:     map[string]float64{},
			expected: math.Inf(-1),
		},
		{
			dict:     map[string]float64{"a": 0},
			expected: 0,
		},
		{
			dict:     map[string]float64{"a": 1, "b": -100, "c": 25},
			expected: 25,
		},
	}

	for i, tc := range testCases {
		tc := tc
		t.Run(fmt.Sprintf("TestMaxValue_%d", i), func(tt *testing.T) {
			tt.Parallel()

			actual := myMaps.MaxValue(tc.dict)

			assert.Equal(
				tt, tc.expected, actual,
				fmt.Sprintf("result must be %f, but got %f", tc.expected, actual),
			)
		})
	}
}
