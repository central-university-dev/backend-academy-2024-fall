package main

import (
	"errors"
	"fmt"
	"github.com/samber/mo"
	"io"
	"iter"
	"log"
	"net/http"
	"os"
	"strings"
	"sync"
	"time"

	"golang.org/x/exp/constraints"
)

// объявляем функцию, которая возвращает функцию, которая возвращает...
func intSeq() func() int {
	i := 0
	return func() int {
		i++
		return i
	}
}

var j = 0

func pIntSeq() func() int {
	return func() int {
		j++
		return j
	}
}

func fib() func() int {
	x, y := 0, 1
	return func() int {
		x, y = y, x+y
		return x
	}
}

func toslice(n int, f func() int) []int {
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

func middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		fmt.Println("Before")
		r.Header.Add("Host", "tbank.ru")
		next.ServeHTTP(w, r)
		w.Header().Set("Content-Type", "application/json")
		fmt.Println("After")
	})
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

	ii := pIntSeq()
	fmt.Println(ii())
	fmt.Println(ii())
	j = -1
	fmt.Println(ii())
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

type (
	mapFunc[E any]    func(E) E
	keepFunc[E any]   func(E) bool
	reduceFunc[E any] func(cur, next E) E
	reduceFuncInt     func(cur, next int) int
	filterFuncInt     func(i int) bool
)

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

func ExampleCallbacks() {
	s := []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	fmt.Println(filterInt(s, isEven))
	fmt.Println(reduceInt(s, 0, sumInts))
}

func Map[S ~[]E, E any](s S, f mapFunc[E]) S {
	result := make(S, len(s))
	for i := range s {
		result[i] = f(s[i])
	}
	return result
}

func Filter[S ~[]E, E any](s S, f keepFunc[E]) S {
	result := S{}
	for _, v := range s {
		if f(v) {
			result = append(result, v)
		}
	}
	return result
}

func Reduce[E any](s []E, init E, f reduceFunc[E]) E {
	cur := init
	for _, v := range s {
		cur = f(cur, v)
	}
	return cur
}

func IsEven[T constraints.Integer](v T) bool {
	return v%2 == 0
}

type Maybe[T any] struct {
	value *T
}

func Just[T any](value T) Maybe[T] {
	return Maybe[T]{value: &value}
}

func Nothing[T any]() Maybe[T] {
	return Maybe[T]{value: nil}
}

func (m Maybe[T]) IsPresent() bool {
	return m.value != nil
}

func (m Maybe[T]) Get() (T, bool) {
	if m.value == nil {
		var zero T
		return zero, false
	}
	return *m.value, true
}

func (m Maybe[T]) Bind(f func(T) Maybe[T]) Maybe[T] {
	if !m.IsPresent() {
		return Nothing[T]()
	}
	return f(*m.value)
}

func (m Maybe[T]) BindErr(f func(T) (Maybe[T], error)) (Maybe[T], error) {
	if !m.IsPresent() {
		return Nothing[T](), nil
	}
	return f(*m.value)
}

func MapExample() {
	s := []string{"he's", "the", "only", "one"}
	fmt.Println(Map(s, strings.ToUpper))
}

func FilterExample() {
	isEven := func(v int) bool {
		return v%2 == 0
	}
	s := []int{1, 2, 3, 4}
	fmt.Println(Filter(s, isEven))

	fmt.Println(Filter(s, IsEven[int]))
}

func ReduceExample() {
	s := []int{1, 2, 3, 4}
	sumFunc := func(cur, next int) int {
		return cur + next
	}
	sum := Reduce(s, 0, sumFunc)
	fmt.Println(sum)
}

func addOne(x int) Maybe[int] {
	return Just(x + 1)
}

func MonadExample() {
	// Create a Just Maybe
	a := Just(5)

	// Use Bind to chain operations
	b := a.Bind(addOne).Bind(addOne).Bind(addOne)

	// Check the result
	if b.IsPresent() {
		value, _ := b.Get()
		fmt.Println("Value:", value) // Output: Value: 8
	} else {
		fmt.Println("No value")
	}

	// Create a Nothing Maybe
	c := Nothing[int]()

	// Use Bind with Nothing
	d := c.Bind(addOne).Bind(addOne).Bind(addOne)

	// Check the result
	if d.IsPresent() {
		value, _ := d.Get()
		fmt.Println("Value:", value)
	} else {
		fmt.Println("No value") // Output: No value
	}
}

func MoExample() {
	m := mo.Some(1)
	//l := mo.None()
	o := m.Match(
		func(value int) (int, bool) {
			return value + 1, true
		},
		func() (int, bool) {
			return 0, false
		},
	).OrElse(0)
	fmt.Println(o)
	res, err := mo.
		NewFuture(func(resolve func(string), reject func(error)) {
			resolve("hello")
		}).
		Then(
			func(s string) (string, error) {
				return "", errors.New("bad string")
			}).
		Catch(
			func(err error) (string, error) {
				return "goodbye", nil
			}).
		Collect()
	fmt.Println(res, err)
}

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

type Option func(opts *options) error

var (
	defaultPort    = 8080
	defaultTimeout = 1 * time.Second
)

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

// https://100go.co/#not-using-the-functional-options-pattern-11
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

func intSeqIt() iter.Seq[int] {
	return func(yield func(int) bool) {
		for i := 0; i < 100; i++ {
			time.Sleep(100 * time.Millisecond)
			if !yield(i) {
				return
			}
		}
	}
}

func ClosuresRealExample() {
	for range intSeqIt() {
		fmt.Println("hello")
	}
}

func add(x, y int) int {
	return x + y
}

func addCpsPlus(x, y int, k func(res int)) {
	if x == 0 {
		// Выходим без вызова продолжения
		return
	}
	if x >= y {
		// Вызываем продолжение дважды - сначала с суммой, потом с нулем
		k(x + y)
		k(0)
	} else {
		// Однократное исполнение продолжения
		k(x + y)
	}
}

func addCps(x, y int, k func(res int)) {
	k(x + y)
}

func ExampleEasyCPS() {
	addCps(1, 2,
		func(res int) {
			fmt.Println(res)
		})
	addCpsPlus(1, 2, func(res int) {
		fmt.Println(res)
	})
}

func writeFile(path, content string) error {
	file, err := os.OpenFile(path, os.O_CREATE|os.O_WRONLY, 0600)
	if err != nil {
		return err
	}
	defer file.Close()

	_, err = file.Write([]byte(content))
	if err != nil {
		return err
	}

	return nil
}

type FileResource = func(cont FileCallback) error

// Функция-продолжение для инкапсуляции бизнес-логики
type FileCallback = func(fd *os.File) error

func WorkWithFile(path string, flags int, perm os.FileMode) FileResource {
	// Каррированный инициализатор ресурса
	return func(cb FileCallback) error {
		// Системные вызовы
		file, err := os.OpenFile(path, flags, perm)
		if err != nil {
			return err
		}
		log.Println("File opened")
		defer func() {
			file.Close()
			log.Println("File closed")
		}()

		// Пользовательская бизнес-логика
		err = cb(file)
		return err
	}
}

func writeToFile(fd *os.File) error {
	log.Println(fd.WriteString("He's only one"))
	return nil
}

func readFromFile(fd *os.File) error {
	b, _ := io.ReadAll(fd)
	log.Println(string(b))
	return nil
}

func ExampleFileResource() {
	// здесь файл еще не откртыт
	fileRes := WorkWithFile("file.txt", os.O_CREATE|os.O_RDWR, 0600)

	// а тут уже да
	_ = fileRes(writeToFile)
	// а тут он уже закрыт

	// передаем этот же ресурс в другую функцию
	// файл опять открывается
	_ = fileRes(readFromFile)
	// и закрывается по завершению функции
}

type Spawner interface {
	Run(task func())
}

type SafeWaitGroup interface {
	Spawner
	Wait()
}

type safeWaitGroup struct {
	wg *sync.WaitGroup
}

func NewSafeWaitGroup() SafeWaitGroup {
	return &safeWaitGroup{new(sync.WaitGroup)}
}

func (swg *safeWaitGroup) Run(task func()) {
	swg.wg.Add(1)
	go func() {
		task()
		swg.wg.Done()
	}()
}

func (swg *safeWaitGroup) Wait() {
	swg.wg.Wait()
}

func RunGroup(taskRunner func(Spawner)) {
	swg := NewSafeWaitGroup()
	taskRunner(swg)
	swg.Wait()
}

func worker(id int) {
	fmt.Printf("Worker %d starting\n", id)
	time.Sleep(time.Second)
	fmt.Printf("Worker %d done\n", id)
}

func lazyWork(id int, run func(id int)) func() {
	return func() {
		run(id)
	}
}

func RunNGroup(n int, worker func(int)) {
	RunGroup(func(spawner Spawner) {
		for i := 0; i < n-1; i++ {
			spawner.Run(lazyWork(i, worker))
		}
	})
}

func ExampleWgCool() {
	RunGroup(func(spawner Spawner) {
		for i := 0; i < 4; i++ {
			i := i
			spawner.Run(func() { worker(i) })
		}
	})
	RunNGroup(5, worker)
}

func main() {
	log.SetFlags(log.Lshortfile | log.LstdFlags)
	//ClosuresExample()
	//FibExample()
	//ExampleMiddleware()
	//ExampleCallbacks()
	//ClosuresRealExample()
	//MapExample()
	//FilterExample()
	//ReduceExample()
	//FunctionalOptionsExample()
	//MonadExample()
	//MoExample()
	//ExampleFileResource()
	//ExampleWgCool()
}
