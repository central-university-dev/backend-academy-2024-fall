package myMaps

func Keys(dict map[string]float64) []string {
	result := make([]string, 0, len(dict))
	for key := range dict {
		result = append(result, key)
	}

	return result
}

func Values(dict map[string]float64) []float64 {
	result := make([]float64, 0, len(dict))
	for _, value := range dict {
		result = append(result, value)
	}

	return result
}
