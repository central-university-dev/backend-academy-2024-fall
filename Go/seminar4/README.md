# Работа с ошибками в Go

- [Работа с ошибками в Go](#работа-с-ошибками-в-go)
  - [Ошибки](#ошибки)
    - [Интерфейс `error`](#интерфейс-error)
    - [Создание собственных ошибок](#создание-собственных-ошибок)
    - [Базовая обработка ошибок](#базовая-обработка-ошибок)
    - [Оборачивание ошибок](#оборачивание-ошибок)
      - [Собственный тип ошибки с оборачиванием](#собственный-тип-ошибки-с-оборачиванием)
      - [Оборачивание множества ошибок](#оборачивание-множества-ошибок)
      - [Обработка ошибок в defer](#обработка-ошибок-в-defer)
    - [Обработка ошибок like a pro](#обработка-ошибок-like-a-pro)
    - [Ошибки в горутинах](#ошибки-в-горутинах)
      - [Errgroup](#errgroup)
  - [Паники](#паники)
    - [Обработка паники](#обработка-паники)

## Ошибки

### Интерфейс `error`

```go
// The error built-in interface type is the conventional interface for
// representing an error condition, with the nil value representing no error.
type error interface {
	Error() string
}
```

По соглашению ошибки в Go возвращаются из функции последним возвращаемым значением. Также, если функция возвращает ошибку, то она не возвращает другие результаты.

```go
func doSomething() (string, error)
```

### Создание собственных ошибок

```go
errors.New("my error") // так делать плохо

type errCustomString string // уже лучше, но все равно так себе

func (e errCustomString) Error() string {
    return string(e)
}

type errCustomStruct struct { // самый лучший вариант
    msg string
}

func (e errCustomStruct) Error() string {
    return fmt.Sprintf("custom error: %s", e.msg)
}
```

---
Сниппет для VSCode для создания ошибок

```json
	"Create Go Error": {
		"scope": "go,golang",
		"prefix": "goerr",
		"body": [
			"//Err$1",
			"type Err$1 struct {",
			"}",
			"",
			"func NewErr$1() error {",
			"\treturn Err$1{}",
			"}",
			"",
			"func (e Err$1) Error() string {",
			"\treturn fmt.Sprintf(\"\")",
			"}"
		],
		"description": "Create a new error type in Go"
	},
```

---

### Базовая обработка ошибок

```go
if err!= nil {
    return err
}
```

### Оборачивание ошибок

```go
func readFile(f os.File) (string, error) {
	buffer := []byte{}
	_, err := f.Read(buffer)

	if err != nil {
		return "", fmt.Errorf("read file: %w", err)
	}

	return string(buffer), nil
}
```

Как делали раньше:

```go
fmt.Errorf("read file: %s", err.Error())
fmt.Errorf("read file: %v", err)
```

Множественное оборачивание ошибок:

```go
err1 := errors.New("err1")
err2 := fmt.Errorf("err2: %w", err1)
err3 := fmt.Errorf("err3: %w", err2)

fmt.Println(err3.Error()) // err3: err2: err1
```

#### Собственный тип ошибки с оборачиванием

```go
type ErrReadFile struct {
    err error
}

func (e ErrReadFile) Error() string {
    return fmt.Sprintf("read file: %s", e.err.Error)
}

func (e ErrReadFile)Unwrap() error { // []error
    return e.err
}

```

#### Оборачивание множества ошибок

Для объединения множества ошибок можно использовать `errors.Join` и оборачивать результат в другую ошибку.

```go
func main() {
	err1 := errors.New("err1")
	err2 := errors.New("err2")
	err := errors.Join(err1, err2)
	fmt.Println(err)
	if errors.Is(err, err1) {
		fmt.Println("err is err1")
	}
	if errors.Is(err, err2) {
		fmt.Println("err is err2")
	}
}
```

#### Обработка ошибок в defer

```go
func readFile(path string) (content string, err error) {
	f, err := os.Open(path)
	if err != nil {
		return "", fmt.Errorf("open file: %w", err)
	}
	defer func() {
		cErr := f.Close()
		if cErr != nil {
			if err == nil {
				err = cErr
			}
			err = errors.Join(err, cErr)
		}
	}()

	buffer := []byte{}
	_, err = f.Read(buffer)

	if err != nil {
		return "", fmt.Errorf("read file: %w", err)
	}

	return string(buffer), nil
}
```

### Обработка ошибок like a pro

```go
err := doSmth()
if err!= nil {
    if errors.As(err, &errCustom{}) {
        // обработка конкретной ошибки
    }
    return fmt.Errorf("undefined error: %w", err)
}
```

Получение экземпляра ошибки в errors.As:

```go
// errors.go
type ErrCustom struct {
    Counter int
}

func (e ErrCustom) Error() string {
    return fmt.Sprintf("custom error with counter %d", e.Counter)
}

// main.go
err := doSmth()
if err!= nil {
    cErr := &ErrCustom{}
    if errors.As(err, cErr) {
        fmt.Println(cErr.Counter)
    }
    return fmt.Errorf("undefined error: %w", err)
}
```

Или вариант покороче

```go
err := doSmth()
if err!= nil {
    if cErr := new(errCustom); errors.As(err, cErr) {
        fmt.Println(cErr.Counter)
    }
    return fmt.Errorf("undefined error: %w", err)
}
```

---

Немного легаси:

```go
// errors.go

var ErrNoRows = errors.New("no rows in result set")

// main.go

func doSmth() error {
    err := doAnother()
    if err!= nil {
        if errors.Is(err, pgx.ErrNoRows) {
            // обработка конкретной ошибки
        }
        return fmt.Errorf("undefined error: %w", err)
    }
}
```

### Ошибки в горутинах

```go
func calculate(input <-chan int, err chan<- error) {
    // do something
}

func main() {
    input := make(chan int)
    err := make(chan error)
    go calculate(input, err)
    for i := 0; i < 10; i++ {
        input <- i
        if err := <-err; err!= nil {
            close(input)
            return err
        }
    }
}
```

#### Errgroup

```go
import "golang.org/x/sync/errgroup"

func calculate(input int) error {
    // do something
}

func main() {
	eg := errgroup.WithContext(context.Background())
	for i := 0; i < 10; i++ {
		eg.Go(func() error {
			return calculate(i)
		})
	}

	return eg.Wait()
}
```

## Паники

Паника - это ошибка, которую на условном слое приложения невозможно обработать.

__Не паникуйте!__

```go
func main() {
    panic("panic")
    panic(fmt.Errorf("panic"))
}
```

### Обработка паники

```go
func f()(err error) {
        defer func() {
        if r := recover(); r != nil {
            err = fmt.Errorf("panic: %v", r)
        }
    }()
    panic("hi there")
}
```
