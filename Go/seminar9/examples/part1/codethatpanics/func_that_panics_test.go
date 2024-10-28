package codeThatPanics_test

import (
	"testing"

	codeThatPanics "backend-academy/Go/seminar9/examples/part1/codethatpanics"
)

func TestFuncThatPanics(t *testing.T) {
	args := []int{1, 2}
	mustPanic := []bool{false, true}

	for i := 0; i < len(args); i++ {
		panicked := false
		func() {
			defer func() {
				if r := recover(); r != nil {
					panicked = true
				}
			}()

			codeThatPanics.FuncThatPanics(args[i])
		}()

		if panicked != mustPanic[i] {
			t.Errorf("panicked: %v, must: %v", panicked, mustPanic[i])
		}
	}
}
