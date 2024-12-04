package main

import (
	"fmt"
	"time"
)

func main() {
	const defaultBufferSize = 2

	const topicName = "config-reloader"

	broker := NewBroker(defaultBufferSize)
	go func() {
		defer broker.Close()

		const (
			ticksQty  = 3
			tickSleep = 500 * time.Millisecond
		)
		for i := 0; i < ticksQty; i++ {
			time.Sleep(tickSleep)

			broker.Publish(topicName, "config data have been reloaded")
		}
	}()

	sub := broker.Subscribe(topicName)
	for msg := range sub {
		fmt.Println(msg)
	}
}
