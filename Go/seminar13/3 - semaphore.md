# Семафор

Семафор нужен, когда требуется ограничить количество горутин, которые могут одновременно работать 
с общим для них ресурсом, например, БД или JSON REST сервисом.

Мьютекс является частным случаем семафора с лимитом 1. Семафор с лимитом N проще всего реализовать
на базе буферизированного канала с размером буфера N.

```go
package main

type Semaphore chan struct{}

func NewSemaphore(limit int) Semaphore {
    return make(Semaphore, limit)
}

func (s Semaphore) Acquire() {
    s <- struct{}{}
}

func (s Semaphore) Release() {
    <-s
}

func main() {
    sema := NewSemaphore(3)
    for i := 0; i < 10; i++ {
        go func(semaphore Semaphore) {
            semaphore.Acquire()
            defer semaphore.Release()
        }(sema)
    }
}
```
