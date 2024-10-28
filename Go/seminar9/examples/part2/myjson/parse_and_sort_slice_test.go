package myJson_test

import (
	"fmt"
	"io"
	"strings"
	"testing"
	"testing/iotest"

	"github.com/stretchr/testify/assert"

	myJson "backend-academy/Go/seminar9/examples/part2/myjson"
)

func TestParseAndSortSlice(t *testing.T) {
	t.Parallel()

	type TestCase struct {
		name     string
		input    io.Reader
		expected []int
		err      error
	}

	testCases := []TestCase{
		{
			name:     "nil input",
			input:    nil,
			expected: nil,
			err:      fmt.Errorf("input is nil"),
		},
		{
			name:     "failed to read",
			input:    iotest.ErrReader(fmt.Errorf("test error 1")),
			expected: nil,
			err:      fmt.Errorf("failed to read data from input stream: test error 1"),
		},
		{
			name:     "failed to parse 1",
			input:    strings.NewReader("invalid json"),
			expected: nil,
			err:      fmt.Errorf("failed to unmarshal data: invalid character 'i' looking for beginning of value"),
		},
		{
			name:     "failed to parse 2",
			input:    strings.NewReader(""),
			expected: nil,
			err:      fmt.Errorf("failed to unmarshal data: unexpected end of JSON input"),
		},
		{
			name:     "failed to parse 3",
			input:    strings.NewReader("[1.23, 4, 5]"),
			expected: nil,
			err:      fmt.Errorf("failed to unmarshal data: json: cannot unmarshal number 1.23 into Go value of type int"),
		},
		{
			name:     "success 1",
			input:    strings.NewReader("[]"),
			expected: []int{},
			err:      nil,
		},
		{
			name:     "success 2",
			input:    strings.NewReader("[3, 2, 1, 2]"),
			expected: []int{1, 2, 2, 3},
			err:      nil,
		},
	}

	for _, tc := range testCases {
		tc := tc
		t.Run(tc.name, func(tt *testing.T) {
			tt.Parallel()

			actual, err := myJson.ParseAndSortSlice(tc.input)

			a := assert.New(tt)
			if tc.err != nil {
				a.EqualError(err, tc.err.Error())

				return
			}

			a.Nil(err) // или a.NoError(err)
			a.Equal(tc.expected, actual)
		})
	}
}
