# convey

Пакет `github.com/smartystreets/goconvey/convey` -- ещё один хороший вспомогательный инструмент для тестов,
альтернатива `github.com/stretchr/testify`.

Пакет `github.com/smartystreets/goconvey/convey` принято импортировать в глобальную область видимости 
(хотя это не обязательно).

Несколько примеров, взятых из `github.com/smartystreets/goconvey/examples`:

[TestIntegerManipulation](./examples/part5/convey/simple_example_test.go)

[TestAssertionsAreAvailableFromConveyPackage](./examples/part5/convey/assertions_examples_test.go)

Каждый вызов `Convey` ограничивает область для тест-кейса. Допускается вложенность.

Порядок выполнения тест-кейсов может отличаться от того, в котором они объявлены!
