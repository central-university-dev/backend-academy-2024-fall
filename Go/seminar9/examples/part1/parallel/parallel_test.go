package parallel_test

import (
	"testing"

	"backend-academy/Go/seminar9/examples/part1/parallel"
)

func TestDouble(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		name     string
		given    int
		expected int
	}

	testCases := []TestCase{
		{
			name:     "Double(0) = 0",
			given:    0,
			expected: 0,
		},
		{
			name:     "Double(1) = 2",
			given:    1,
			expected: 2,
		},
		{
			name:     "Double(-2) = -4",
			given:    -2,
			expected: -4,
		},
	}

	for _, testCase := range testCases {
		testCase := testCase
		t.Run(testCase.name, func(tt *testing.T) {
			tt.Parallel()

			actual := parallel.Double(testCase.given)
			if testCase.expected != actual {
				t.Errorf("expected: %d, got: %d", testCase.expected, actual)
			}
		})
	}
}

func TestSquare(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		name     string
		given    int
		expected int
	}

	testCases := []TestCase{
		{
			name:     "Square(0) = 0",
			given:    0,
			expected: 0,
		},
		{
			name:     "Square(1) = 1",
			given:    1,
			expected: 1,
		},
		{
			name:     "Square(-2) = 4",
			given:    -2,
			expected: 4,
		},
	}

	for _, testCase := range testCases {
		testCase := testCase
		t.Run(testCase.name, func(tt *testing.T) {
			tt.Parallel()

			actual := parallel.Square(testCase.given)
			if testCase.expected != actual {
				t.Errorf("expected: %d, got: %d", testCase.expected, actual)
			}
		})
	}
}

func TestCube(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		name     string
		given    int
		expected int
	}

	testCases := []TestCase{
		{
			name:     "Cube(0) = 0",
			given:    0,
			expected: 0,
		},
		{
			name:     "Cube(-1) = -1",
			given:    -1,
			expected: -1,
		},
		{
			name:     "Cube(4) = 64",
			given:    4,
			expected: 64,
		},
	}

	for _, testCase := range testCases {
		testCase := testCase
		t.Run(testCase.name, func(tt *testing.T) {
			tt.Parallel()

			actual := parallel.Cube(testCase.given)
			if testCase.expected != actual {
				t.Errorf("expected: %d, got: %d", testCase.expected, actual)
			}
		})
	}
}
