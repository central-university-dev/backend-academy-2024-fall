- [testify](#testify)
  - [assert/require](#assertrequire)
  - [suite](#suite)

# testify

Пакет `github.com/stretchr/testify` содержит множество функций для более наглядной организации 
тест-кейсов. 

## assert/require

Пакеты `github.com/stretchr/testify/assert` и `github.com/stretchr/testify/require` предоставляют идентичные
наборы функций. Разница между этими двумя пакетами в том же, в чём и между `t.Error(f)` и `t.Fatal(f)`.

Т.е., если не будет пройдена проверка из `github.com/stretchr/testify/require`, то тест сразу будет завершён 
как ошибочный, а если не пройдёт проверка из `github.com/stretchr/testify/assert`, то тест будет выполнен 
до конца (и тоже завершён как ошибочный).

Типичный вызов из пакета выглядит таким образом: 
`assert.Equal(t, expectedVal, actualVal, "some error message")`. 

Это служит более удобной и понятной заменой для
```go
if expectedVal != actualVal {
    t.Error("some error message")
}
```

[Примеры с assert.Equal и assert.ElementsMatch](./examples/part2/mymaps)

[Примеры с assert.Nil, assert.NotNil и assert.EqualError](./examples/part2/myjson)

## suite

Пакет `github.com/stretchr/testify/suite` служит для более сложной организации набора тестов,
когда требуется производить одинаковые наборы действий перед и/или после каждого теста.

Набор тестов (suite) оформляется в виде структуры, содержащей опциональные функции-ресиверы
`SetupSuite` и `TearDownSuite` для соответственно инициализации и финализации всего набора.

Ещё можно объявить функции-ресиверы `SetupTest` и `TearDownTest`, которые выполняют аналогичные
действия до и после каждого теста.

Все тесты в наборе объявляются в виде функций-ресиверов, имя которых начинается с префикса `Test`.
**suite не поддерживает параллельный запуск тестов; все тесты запускаются последовательно.**

[Пример использования suite](./examples/part2/users)
