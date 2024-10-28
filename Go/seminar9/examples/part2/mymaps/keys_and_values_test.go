package myMaps_test

import (
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"

	myMaps "backend-academy/Go/seminar9/examples/part2/mymaps"
)

func TestKeysAndValues(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		dict           map[string]float64
		expectedKeys   []string
		expectedValues []float64
	}

	testCases := []TestCase{
		{
			dict:           nil,
			expectedKeys:   []string{},
			expectedValues: []float64{},
		},
		{
			dict:           map[string]float64{},
			expectedKeys:   []string{},
			expectedValues: []float64{},
		},
		{
			dict:           map[string]float64{"a": 0},
			expectedKeys:   []string{"a"},
			expectedValues: []float64{0},
		},
		{
			dict:           map[string]float64{"a": 1, "b": -100, "c": 25},
			expectedKeys:   []string{"c", "b", "a"},
			expectedValues: []float64{1, 25, -100},
		},
		{
			dict:           map[string]float64{"a": 1, "b": 2, "c": 1},
			expectedKeys:   []string{"a", "b", "c"},
			expectedValues: []float64{1, 1, 2},
		},
	}

	for i, tc := range testCases {
		tc := tc
		t.Run(fmt.Sprintf("TestKeysAndValues_%d", i), func(tt *testing.T) {
			tt.Parallel()

			actualKeys := myMaps.Keys(tc.dict)
			actualValues := myMaps.Values(tc.dict)

			a := assert.New(tt)
			a.ElementsMatch(tc.expectedKeys, actualKeys)
			a.ElementsMatch(tc.expectedValues, actualValues)
		})
	}
}
