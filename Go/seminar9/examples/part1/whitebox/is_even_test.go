package main

import (
	"testing"
)

func TestPrivateIsEven(t *testing.T) {
	if !privateIsEven(0) {
		t.Error("0 is not even")
	}
	if privateIsEven(1) {
		t.Error("1 is even")
	}
}
