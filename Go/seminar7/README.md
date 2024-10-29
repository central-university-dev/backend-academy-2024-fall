# Работа с сетью в Go

- [Работа с сетью в Go](#работа-с-сетью-в-go)
  - [1. Создание TCP-сервера](#1-создание-tcp-сервера)
    - [Пример TCP-сервера](#пример-tcp-сервера)
    - [Основные моменты:](#основные-моменты)
    - [2. Создание UDP-сервера](#2-создание-udp-сервера)
    - [Пример UDP-сервера](#пример-udp-сервера)
    - [Основные моменты](#основные-моменты-1)
  - [3. Создание HTTP-сервера](#3-создание-http-сервера)
    - [Пример HTTP-сервера](#пример-http-сервера)
    - [Основные моменты](#основные-моменты-2)
  - [4. HTTP Mux (Router)](#4-http-mux-router)
    - [Пример использования `http.ServeMux`](#пример-использования-httpservemux)
    - [Основные моменты](#основные-моменты-3)
    - [5. Кастомизация HTTP-сервера](#5-кастомизация-http-сервера)
    - [Пример кастомного сервера](#пример-кастомного-сервера)
    - [Основные моменты](#основные-моменты-4)
  - [6. Создание HTTP-клиента](#6-создание-http-клиента)
    - [Пример HTTP-клиента](#пример-http-клиента)
    - [Основные моменты](#основные-моменты-5)
  - [7. Создание TCP-клиента](#7-создание-tcp-клиента)
    - [Пример TCP-клиента](#пример-tcp-клиента)
    - [Основные моменты](#основные-моменты-6)
    - [8. Создание UDP-клиента](#8-создание-udp-клиента)
    - [Пример UDP-клиента](#пример-udp-клиента)
    - [Основные моменты](#основные-моменты-7)
  - [9. Закрытие `Body` в HTTP-клиенте](#9-закрытие-body-в-http-клиенте)
  - [10. Работа с TLS (Transport Layer Security)](#10-работа-с-tls-transport-layer-security)
    - [10.1. Создание TLS HTTP-сервера](#101-создание-tls-http-сервера)
    - [Пример TLS HTTP-сервера](#пример-tls-http-сервера)
    - [Основные моменты](#основные-моменты-8)
    - [10.2. Создание TLS HTTP-клиента](#102-создание-tls-http-клиента)
    - [Пример TLS HTTP-клиента](#пример-tls-http-клиента)
    - [Основные моменты](#основные-моменты-9)
  - [11. Graceful Shutdown для серверов](#11-graceful-shutdown-для-серверов)
    - [11.1. Graceful Shutdown для HTTP-сервера](#111-graceful-shutdown-для-http-сервера)
    - [Пример Graceful Shutdown для HTTP-сервера](#пример-graceful-shutdown-для-http-сервера)
    - [Основные моменты](#основные-моменты-10)
      - [11.2. Graceful Shutdown для TCP-сервера](#112-graceful-shutdown-для-tcp-сервера)
    - [Пример Graceful Shutdown для TCP-сервера](#пример-graceful-shutdown-для-tcp-сервера)
    - [Основные моменты](#основные-моменты-11)
    - [11.3. Graceful Shutdown для UDP-сервера](#113-graceful-shutdown-для-udp-сервера)
    - [Пример Graceful Shutdown для UDP-сервера](#пример-graceful-shutdown-для-udp-сервера)
    - [Основные моменты](#основные-моменты-12)
    - [12. Заключение](#12-заключение)


---

## 1. Создание TCP-сервера

TCP-сервер устанавливает надежное соединение с клиентом и передает данные с гарантией доставки.

### Пример TCP-сервера

```go
package main

import (
    "bufio"
    "fmt"
    "net"
)

func main() {
    // Создаем слушателя на определенном порту
    listener, err := net.Listen("tcp", ":8080")
    if err != nil {
        panic(err)
    }
    defer listener.Close()

    fmt.Println("TCP-сервер запущен на порту 8080")

    for {
        conn, err := listener.Accept()
        if err != nil {
            fmt.Println(err)
            continue
        }

        go handleConnection(conn) // Обрабатываем соединение в отдельной горутине
    }
}

func handleConnection(conn net.Conn) {
    defer conn.Close()
    
    // Чтение данных из соединения
    message, _ := bufio.NewReader(conn).ReadString('\n')
    fmt.Println("Получено сообщение:", message)
    
    // Отправка ответа клиенту
    conn.Write([]byte("Привет от сервера!\n"))
}
```

Чтоб проверить используем netcat `echo hello | nc localhost 8080`


### Основные моменты:
- `net.Listen("tcp", ":8080")` — создаем слушателя на порту 8080.
- `conn, err := listener.Accept()` — принимаем входящее соединение.
- `conn.Write([]byte("..."))` — отправляем ответ клиенту.
- Закрываем соединение с помощью `conn.Close()`.

---

### 2. Создание UDP-сервера

UDP — это менее надежный, но более быстрый протокол, который не гарантирует доставку пакетов.

### Пример UDP-сервера

```go
package main

import (
    "fmt"
    "net"
)

func main() {
    addr, err := net.ResolveUDPAddr("udp", ":8080")
    if err != nil {
        panic(err)
    }

    conn, err := net.ListenUDP("udp", addr)
    if err != nil {
        panic(err)
    }
    defer conn.Close()

    fmt.Println("UDP-сервер запущен на порту 8080")

    buffer := make([]byte, 1024)
    for {
        n, clientAddr, err := conn.ReadFromUDP(buffer)
        if err != nil {
            fmt.Println(err)
            continue
        }

        fmt.Printf("Получено сообщение: %s от %s\n", string(buffer[:n]), clientAddr)

        // Отправка ответа клиенту
        conn.WriteToUDP([]byte("Привет от UDP-сервера!"), clientAddr)
    }
}
```

### Основные моменты

- `net.ResolveUDPAddr` — разрешение адреса для UDP.
- `conn.ReadFromUDP` — чтение данных от клиента.
- `conn.WriteToUDP` — отправка данных клиенту.

---

## 3. Создание HTTP-сервера

HTTP-серверы обрабатывают веб-запросы. Go предоставляет встроенные функции для работы с HTTP.

### Пример HTTP-сервера

```go
package main

import (
    "fmt"
    "net/http"
)

func handler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Привет, мир!")
}

func main() {
    http.HandleFunc("/", handler)

    fmt.Println("HTTP-сервер запущен на порту 8080")
    http.ListenAndServe(":8080", nil)
}
```

### Основные моменты

- `http.HandleFunc("/", handler)` — связывает URL с обработчиком запросов.
- `w.Write` или `fmt.Fprintln(w, ...)` — записываем ответ клиенту.
- `http.ListenAndServe(":8080", nil)` — запускаем сервер на порту 8080.

---

## 4. HTTP Mux (Router)

Для более сложной маршрутизации запросов можно использовать `http.ServeMux`.

### Пример использования `http.ServeMux`

```go
package main

import (
    "fmt"
    "net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Привет!")
}

func goodbyeHandler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Пока!")
}

func main() {
    mux := http.NewServeMux()
    mux.HandleFunc("/hello", helloHandler)
    mux.HandleFunc("/goodbye", goodbyeHandler)

    fmt.Println("HTTP-сервер с маршрутизацией запущен на порту 8080")
    http.ListenAndServe(":8080", mux)
}
```

### Основные моменты

- `http.NewServeMux()` — создаем новый мультиплексор (маршрутизатор).
- `mux.HandleFunc("/path", handler)` — задаем маршрут.

---

### 5. Кастомизация HTTP-сервера

HTTP-сервер может быть кастомизирован через структуру `http.Server`.

### Пример кастомного сервера

```go
package main

import (
    "net/http"
    "time"
)

func handler(w http.ResponseWriter, r *http.Request) {
    w.Write([]byte("Настроенный сервер!"))
}

func main() {
    srv := &http.Server{
        Addr:         ":8080",
        Handler:      http.HandlerFunc(handler),
        ReadTimeout:  10 * time.Second,
        WriteTimeout: 10 * time.Second,
    }

    srv.ListenAndServe()
}
```

### Основные моменты

- `ReadTimeout`, `WriteTimeout` — задают максимальное время на чтение и запись.
- Кастомизация возможна для SSL/TLS, таймаутов, логов и прочего.

---

## 6. Создание HTTP-клиента

Для выполнения HTTP-запросов можно использовать `http.Client`.

### Пример HTTP-клиента

```go
package main

import (
    "fmt"
    "io/ioutil"
    "net/http"
)

func main() {
    resp, err := http.Get("https://example.com")
    if err != nil {
        panic(err)
    }
    defer resp.Body.Close()

    body, _ := ioutil.ReadAll(resp.Body)
    fmt.Println(string(body))
}
```

### Основные моменты

- `http.Get("URL")` — выполняем GET-запрос.
- `resp.Body.Close()` — важно закрыть тело ответа после его использования.
- Используем `ioutil.ReadAll` для чтения данных из ответа.

---

## 7. Создание TCP-клиента

TCP-клиенты могут подключаться к TCP-серверам и отправлять данные.

### Пример TCP-клиента

```go
package main

import (
    "bufio"
    "fmt"
    "net"
)

func main() {
    conn, err := net.Dial("tcp", "localhost:8080")
    if err != nil {
        panic(err)
    }
    defer conn.Close()

    fmt.Fprintf(conn, "Привет, сервер!\n")
    message, _ := bufio.NewReader(conn).ReadString('\n')
    fmt.Println("Ответ от сервера:", message)
}
```

### Основные моменты

- `net.Dial("tcp", "localhost:8080")` — подключаемся к TCP-серверу.
- Используем `bufio.Reader` для чтения ответа от сервера.

---

### 8. Создание UDP-клиента

UDP-клиент отправляет сообщения на UDP-сервер без необходимости поддерживать соединение.

### Пример UDP-клиента

```go
package main

import (
    "fmt"
    "net"
)

func main() {
    addr, err := net.ResolveUDPAddr("udp", "localhost:8080")
    if err != nil {
        panic(err)
    }

    conn, err := net.DialUDP("udp", nil, addr)
    if err != nil {
        panic(err)
    }
    defer conn.Close()

    conn.Write([]byte("Привет, UDP-сервер!"))

    buffer := make([]byte, 1024)
    n, _, err := conn.ReadFromUDP(buffer)
    if err != nil {
        panic(err)
    }

    fmt.Println("Ответ от сервера:", string(buffer[:n]))
}
```

### Основные моменты

- `net.DialUDP` — подключаемся к UDP-серверу.
- `conn.ReadFromUDP` — читаем данные от сервера.

---

## 9. Закрытие `Body` в HTTP-клиенте

Важно **всегда закрывать** тело ответа, чтобы избежать утечек ресурсов:

```go
resp, err := http.Get("https://example.com")
if err != nil {
    log.Fatal(err)
}
defer resp.Body.Close()
```

---

## 10. Работа с TLS (Transport Layer Security)

TLS обеспечивает безопасное шифрованное соединение между клиентом и сервером. В Go поддержка TLS встроена в стандартную библиотеку.

### 10.1. Создание TLS HTTP-сервера

Для создания HTTPS-сервера необходимо предоставить сертификат и приватный ключ.

### Пример TLS HTTP-сервера

```go
package main

import (
    "fmt"
    "net/http"
)

func handler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Безопасное соединение с TLS!")
}

func main() {
    http.HandleFunc("/", handler)

    fmt.Println("HTTPS-сервер запущен на порту 8443")
    err := http.ListenAndServeTLS(":8443", "server.crt", "server.key", nil)
    if err != nil {
        panic(err)
    }
}
```

### Основные моменты

- `http.ListenAndServeTLS` — запускает HTTPS-сервер.
- Необходимо предоставить пути к сертификату (`server.crt`) и приватному ключу (`server.key`).
- Сертификат можно создать самостоятельно для тестирования с помощью `openssl`:
  ```bash
  openssl req -x509 -newkey rsa:4096 -keyout server.key -out server.crt -days 365 -nodes
  ```

### 10.2. Создание TLS HTTP-клиента

Клиент также может устанавливать безопасное соединение.

### Пример TLS HTTP-клиента

```go
package main

import (
    "crypto/tls"
    "fmt"
    "io/ioutil"
    "net/http"
)

func main() {
    // Настраиваем TLS конфигурацию
    tlsConfig := &tls.Config{
        InsecureSkipVerify: true, // Не проверять сертификат (для тестирования)
    }

    transport := &http.Transport{
        TLSClientConfig: tlsConfig,
    }

    client := &http.Client{
        Transport: transport,
    }

    resp, err := client.Get("https://localhost:8443")
    if err != nil {
        panic(err)
    }
    defer resp.Body.Close()

    body, _ := ioutil.ReadAll(resp.Body)
    fmt.Println(string(body))
}
```

### Основные моменты

- `tls.Config` — конфигурация TLS, например, `InsecureSkipVerify` отключает проверку сертификата (не рекомендуется для продакшена).
- Используем кастомный `http.Transport` с заданной TLS-конфигурацией.
- `http.Client` использует этот транспорт для запросов.

---

## 11. Graceful Shutdown для серверов

Graceful Shutdown позволяет серверу корректно завершить работу, обработав текущие запросы перед завершением.

### 11.1. Graceful Shutdown для HTTP-сервера

### Пример Graceful Shutdown для HTTP-сервера

```go
package main

import (
    "context"
    "fmt"
    "net/http"
    "os"
    "os/signal"
    "time"
)

func handler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Сервер работает!")
}

func main() {
    mux := http.NewServeMux()
    mux.HandleFunc("/", handler)

    srv := &http.Server{
        Addr:    ":8080",
        Handler: mux,
    }

    // Запуск сервера в отдельной горутине
    go func() {
        if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
            fmt.Printf("Ошибка запуска сервера: %s\n", err)
        }
    }()

    fmt.Println("HTTP-сервер запущен на порту 8080")

    // Ожидание сигнала завершения (например, Ctrl+C)
    quit := make(chan os.Signal, 1)
    signal.Notify(quit, os.Interrupt)
    <-quit
    fmt.Println("Получен сигнал завершения. Остановка сервера...")

    // Контекст с таймаутом для graceful shutdown
    ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
    defer cancel()

    if err := srv.Shutdown(ctx); err != nil {
        fmt.Printf("Ошибка при остановке сервера: %s\n", err)
    }

    fmt.Println("Сервер успешно остановлен")
}
```

### Основные моменты

- Используем `os/signal` для прослушивания системных сигналов (например, SIGINT).
- `srv.Shutdown(ctx)` инициирует graceful shutdown, позволяя серверу обработать текущие запросы.
- Устанавливаем таймаут для завершения работы сервера.

#### 11.2. Graceful Shutdown для TCP-сервера

Graceful Shutdown для TCP-сервера требует более ручного подхода, так как стандартная библиотека Go не предоставляет встроенных средств для этого.

### Пример Graceful Shutdown для TCP-сервера

```go
package main

import (
    "bufio"
    "fmt"
    "net"
    "os"
    "os/signal"
    "sync"
    "syscall"
)

func main() {
    listener, err := net.Listen("tcp", ":8080")
    if err != nil {
        panic(err)
    }
    defer listener.Close()

    fmt.Println("TCP-сервер запущен на порту 8080")

    var wg sync.WaitGroup
    quit := make(chan os.Signal, 1)
    signal.Notify(quit, os.Interrupt, syscall.SIGTERM)

    go func() {
        <-quit
        fmt.Println("Получен сигнал завершения. Закрываем слушателя...")
        listener.Close()
    }()

    for {
        conn, err := listener.Accept()
        if err != nil {
            select {
            case <-quit:
                // Завершаем работу сервера
                fmt.Println("Слушатель закрыт. Ожидание завершения горутин...")
                wg.Wait()
                fmt.Println("Сервер успешно остановлен")
                return
            default:
                fmt.Println("Ошибка при приеме соединения:", err)
                continue
            }
        }

        wg.Add(1)
        go handleConnection(conn, &wg)
    }
}

func handleConnection(conn net.Conn, wg *sync.WaitGroup) {
    defer wg.Done()
    defer conn.Close()

    message, err := bufio.NewReader(conn).ReadString('\n')
    if err != nil {
        fmt.Println("Ошибка чтения сообщения:", err)
        return
    }
    fmt.Println("Получено сообщение:", message)

    conn.Write([]byte("Привет от сервера!\n"))
}
```

### Основные моменты

- Используем `os/signal` для прослушивания сигналов завершения.
- При получении сигнала закрываем слушатель с помощью `listener.Close()`.
- Используем `sync.WaitGroup` для ожидания завершения всех активных горутин.
- Обрабатываем ошибку `listener.Accept()` при закрытии слушателя, чтобы корректно завершить работу.

### 11.3. Graceful Shutdown для UDP-сервера

UDP-серверы обычно не устанавливают постоянные соединения, поэтому graceful shutdown сводится к закрытию сокета и завершению обработки.

### Пример Graceful Shutdown для UDP-сервера

```go
package main

import (
    "fmt"
    "net"
    "os"
    "os/signal"
    "syscall"
    "sync"
)

func main() {
    addr, err := net.ResolveUDPAddr("udp", ":8080")
    if err != nil {
        panic(err)
    }

    conn, err := net.ListenUDP("udp", addr)
    if err != nil {
        panic(err)
    }
    defer conn.Close()

    fmt.Println("UDP-сервер запущен на порту 8080")

    var wg sync.WaitGroup
    quit := make(chan os.Signal, 1)
    signal.Notify(quit, os.Interrupt, syscall.SIGTERM)

    go func() {
        <-quit
        fmt.Println("Получен сигнал завершения. Закрываем UDP-соединение...")
        conn.Close()
    }()

    buffer := make([]byte, 1024)
    for {
        n, clientAddr, err := conn.ReadFromUDP(buffer)
        if err != nil {
            select {
            case <-quit:
                // Завершаем работу сервера
                fmt.Println("UDP-соединение закрыто. Ожидание завершения горутин...")
                wg.Wait()
                fmt.Println("UDP-сервер успешно остановлен")
                return
            default:
                fmt.Println("Ошибка чтения из UDP:", err)
                continue
            }
        }

        wg.Add(1)
        go handleUDPConnection(conn, clientAddr, buffer[:n], &wg)
    }
}

func handleUDPConnection(conn *net.UDPConn, addr *net.UDPAddr, data []byte, wg *sync.WaitGroup) {
    defer wg.Done()
    message := string(data)
    fmt.Printf("Получено сообщение: %s от %s\n", message, addr)

    _, err := conn.WriteToUDP([]byte("Привет от UDP-сервера!"), addr)
    if err != nil {
        fmt.Println("Ошибка отправки ответа:", err)
    }
}
```

### Основные моменты

- Закрываем UDP-соединение при получении сигнала завершения.
- Используем `sync.WaitGroup` для ожидания завершения всех активных горутин.
- Обрабатываем ошибки чтения после закрытия соединения для корректного завершения работы.

---

### 12. Заключение

Работа с сетью в Go включает множество деталей, от создания серверов на различных протоколах до тонкой настройки клиентов и серверов. Важно соблюдать best practices, такие как закрытие ресурсов, правильная обработка ошибок, настройка таймаутов, обеспечение безопасности с помощью TLS и корректное завершение работы серверов с помощью graceful shutdown. Эти навыки помогут создавать надежные и безопасные сетевые приложения на языке Go.
