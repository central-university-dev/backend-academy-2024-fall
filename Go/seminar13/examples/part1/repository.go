package main

import (
	"sync"
)

type Broker struct {
	mu                 sync.Mutex
	subscribersByTopic map[string][]chan string
	defaultBufferSize  int
	closed             bool
}

func NewBroker(defaultBufferSize int) *Broker {
	return &Broker{
		subscribersByTopic: make(map[string][]chan string),
		defaultBufferSize:  defaultBufferSize,
	}
}

func (b *Broker) Close() {
	b.mu.Lock()
	defer b.mu.Unlock()

	if b.closed {
		return
	}

	b.closed = true
	for _, subscribers := range b.subscribersByTopic {
		for _, sub := range subscribers {
			close(sub)
		}
	}
}

func (b *Broker) Publish(topic string, data string) {
	b.mu.Lock()
	defer b.mu.Unlock()

	if b.closed {
		return
	}

	topicSubs := b.subscribersByTopic[topic]
	for _, sub := range topicSubs {
		sub <- data
	}
}

func (b *Broker) Subscribe(topic string) <-chan string {
	b.mu.Lock()
	defer b.mu.Unlock()

	if b.closed {
		return nil
	}

	newSub := make(chan string, b.defaultBufferSize)
	b.subscribersByTopic[topic] = append(b.subscribersByTopic[topic], newSub)

	return newSub
}
