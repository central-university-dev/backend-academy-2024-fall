package myMaps

import (
	"math"
)

func MaxValue(dict map[string]float64) float64 {
	result := math.Inf(-1)
	for _, value := range dict {
		if value > result {
			result = value
		}
	}

	return result
}
