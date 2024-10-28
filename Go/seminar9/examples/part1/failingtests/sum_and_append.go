package failingTests

func SumAndAppend(slice []int) {
	sum := 0
	for _, value := range slice {
		sum += value
	}

	slice = append(slice, sum)
}
