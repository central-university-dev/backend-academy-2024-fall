package even_test

import (
	"testing"

	"backend-academy/Go/seminar9/examples/part1/blackbox/even"
)

func TestIsEven(t *testing.T) {
	if !even.IsEven(0) {
		t.Error("0 is not even")
	}
	if even.IsEven(1) {
		t.Error("1 is even")
	}
}
