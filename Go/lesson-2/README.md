## Замыкания (aka closures)
>Замыкание — это функция, которая запоминает значения из своей внешней области видимости, даже если эта область уже недоступна. Она создается, когда функция объявляется, и продолжает запоминать значения переменных даже после того, как вызывающая функция завершит свою работу.

>Замыкания — это инструмент, который позволяет сохранять значения и состояние между вызовами функций, создавать функции на лету и возвращать их из других функций.
```go
// объявляем функцию, которая возвращает функцию, которая возвращает...
func intSeq() func() int {
	i := 0
	return func() int {
		i++
		return i
	}
}

func ClosuresExample() {
	// здесь nextInt - это функция, которая инкрементит счетчик и возвращает int
	nextInt := intSeq()

	fmt.Println(nextInt())
	fmt.Println(nextInt())
	fmt.Println(nextInt())

	newInt := intSeq()
	for i := 0; i < 10; i++ {
		fmt.Println(newInt())
	}
}
```

#### Вычисление чисел [Фибоначчи](https://ru.wikipedia.org/wiki/%D0%A7%D0%B8%D1%81%D0%BB%D0%B0_%D0%A4%D0%B8%D0%B1%D0%BE%D0%BD%D0%B0%D1%87%D1%87%D0%B8)
```go

// вычисляем числа Фибоначчи подходом динамического программирования
func fib() func() int {
	x, y := 0, 1
	return func() int {
		x, y = y, x+y
		return x
	}
}

// функция toslice принимает генератор f и возвращает срез из n элементов, где каждый элемент - результат вызова f()
func toslice(n int, f func () int) []int {
	s := make([]int, 0, n)
	for i := 0; i < n; i++ {
		s = append(s, f())
	}
	return s
}

func FibExample() {
	f := fib()
	log.Println(f())
	log.Println(f())
	log.Println(f())
	log.Println(f())
	log.Println(f())
	log.Println(toslice(50, fib()))
}
```

#### Middlewares
> Мидлвары (middlewares) позволяют вынести общий функционал из HTTP обработчиков в отдельные функции. Встроенная обработка ошибок позволяет снизить размер HTTP функций и обрабатывать ошибки в мидлварах
```go
func middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		fmt.Println("Before")
		r.Header.Add("Host", "tbank.ru") // выставляем хидер Host
		next.ServeHTTP(w, r)
		w.Header().Set("Content-Type", "application/json")
		fmt.Println("After")
	})
}

func ExampleMiddleware() {
	http.Handle("/", middleware(
		http.HandlerFunc(
			func(w http.ResponseWriter, r *http.Request) {
				fmt.Println("Hello World", r.Header.Get("Host"))
			}),
	),
	)
	http.ListenAndServe(":8080", nil)
}
```

## Коллбэки

```go
// применяет функцию к каждому элементу массива и возвращает агрегат
// f - callback функция определенного типа
func reduceInt(s []int, init int, f reduceFuncInt) int {
	cur := init
	for _, v := range s {
		cur = f(cur, v)
	}
	return cur
}

func sumInts(cur, next int) int {
	return cur + next
}

// функтор для фильтрации данных из слайса интов
func filterInt(s []int, f filterFuncInt) []int {
	res := make([]int, 0, len(s))
	for _, v := range s {
		if f(v) {
			res = append(res, v)
		}
	}
	return res
}

func isEven(i int) bool {
	return i%2 == 0
}
```

Библиотека [https://github.com/samber/lo](https://github.com/samber/lo) вдохновленная [lodash](https://lodash.com/)

## Functional Options 
```go
type options struct {
	port    *int
	host    string
	timeout time.Duration
	maxConn int
}

type Server struct {
	host    string
	port    int
	timeout time.Duration
	maxConn int
}

// опция это функция, которая принимает структуру опций
type Option func(opts *options) error

var (
	defaultPort    = 8080
	defaultTimeout = 1 * time.Second
)

// инициализация сервера возможна как без опций, так и необходимым набором параметров
func New(opts ...Option) (*Server, error) {
	svr := &Server{}
	var o options
	for _, opt := range opts {
		if err := opt(&o); err != nil {
			return svr, err
		}
	}
	if o.port == nil {
		o.port = &defaultPort
	}
	return svr, nil
}

func (s *Server) Start() error {
	return nil
}

func WithHost(host string) Option {
	return func(opts *options) error {
		opts.host = host
		return nil
	}
}

func WithPort(port int) Option {
	return func(opts *options) error {
		if port < 0 {
			return errors.New("port should be positive")
		}
		opts.port = &port
		return nil
	}
}

func WithTimeout(timeout time.Duration) Option {
	return func(opts *options) error {
		if timeout <= 0 {
			timeout = defaultTimeout
		}
		opts.timeout = timeout
		return nil
	}
}

func WithMaxConn(maxConn int) Option {
	return func(opts *options) error {
		opts.maxConn = maxConn
		return nil
	}
}

func FunctionalOptionsExample() {
	svr, err := New(
		WithHost("localhost"),
		WithPort(8080),
		WithTimeout(10*time.Second),
		WithMaxConn(100),
	)

	if err != nil {
		// Handle error
	}
	if err := svr.Start(); err != nil {
		fmt.Println("Error starting server:", err)
	}
}
```
