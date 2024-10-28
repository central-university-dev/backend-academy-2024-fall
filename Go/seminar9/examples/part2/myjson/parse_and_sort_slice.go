package myJson

import (
	"encoding/json"
	"fmt"
	"io"
	"sort"
)

func ParseAndSortSlice(input io.Reader) ([]int, error) {
	if input == nil {
		return nil, fmt.Errorf("input is nil")
	}

	data, err := io.ReadAll(input)
	if err != nil {
		return nil, fmt.Errorf("failed to read data from input stream: %w", err)
	}

	var result []int
	if err = json.Unmarshal(data, &result); err != nil {
		return nil, fmt.Errorf("failed to unmarshal data: %w", err)
	}

	sort.Ints(result)

	return result, nil
}
