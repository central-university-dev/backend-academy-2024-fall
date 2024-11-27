package backend.academy.seminar13;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;
import reactor.util.function.Tuples;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ProjectReactorTest {

    private static final String TEST_DESCRIPTION_KEY = "TEST_DESCRIPTION_KEY";
    private static final Random RANDOM = new Random();

    @Test
    @DisplayName("Основные типы Project Reactor - Mono и Flux")
    void test0() {
        // 1. Два основных типа - Mono и Flux
        // Mono - ноль или один элемент, Flux - ноль или множество элементов (вплоть до бесконечности)
        // Оба реализуют интерфейс Publisher, как и промежуточные операции
        var mono = Mono.just("Hello");
        mono = Flux.fromIterable(List.of("Hello", "World", "!"))
            .map(String::toUpperCase)
            .collect(Collectors.joining(" "));

        // 2. Потоки холодные, и до тех пор, пока не будет хотя бы одно Subscriber'а, выполняться не будут
        log.info("Значение Mono: {}", mono);
        // block - пример такого Subscriber'а, но использовать его противопоказано!
        log.info("Значение Mono: {}", mono.block());

        // 3. Потоки можно породить как на основе уже готовых данных...
        Mono.empty();
        Mono.just("Hello");
        Mono.error(new UnsupportedOperationException());
        Flux.fromIterable(List.of("Hello", "World", "!"));
        Flux.fromStream(Stream.of("Hello", "World", "!"));

        // ..., так и с помощью генераторов
        var randomMono = Mono.create(sink -> {
            // Настраиваем нотификацию на разные события над этим потоком
            sink.onRequest(requested -> log.info("Из данного Mono запрошено {} элементов!", requested));
            sink.onDispose(() -> log.info("Поток был завершен!"));

            // Логика генерации случайного четного числа
            var maxAttempts = 1_000;
            while (maxAttempts-- > 0) {
                var random = RANDOM.nextInt(0, 100);
                if (random % 2 == 0) {
                    sink.success(random);
                    return;
                }
            }

            sink.error(new IllegalStateException("Не удалось сгенерировать четное число!"));
        });

        try {
            log.info("Значение Mono: {}", randomMono.block());
        } catch (Exception e) {
            log.error("Mono завершилось ошибкой!", e);
        }

        // Пример с Flux#generate
        var fibFlux = Flux.generate(() -> Tuples.of(1, 1), (state, sink) -> {
            sink.next(state.getT1());
            return Tuples.of(state.getT2(), state.getT1() + state.getT2());
        });

        fibFlux.take(10)
            .doOnNext(it -> log.info("Следующее число Фибоначчи: {}", it))
            .blockLast();

        // 4. Переход от Flux к Mono и обратно
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .single();
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .last();
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .collectList();
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .collectList()
            .flux();
    }

    @Test
    @DisplayName("Базовые и flat/concat... операции над потоками")
    void test1() {
        Flux<Integer> counterFlux = Flux.generate(() -> 0, (state, sink) -> {
            sink.next(state);
            return state + 1;
        });

        // Note: здесь можно поиграться, и посмотреть на другие операции
        var firstValueUnderHundred = counterFlux
            // .log() // Можно включить, чтобы увидеть в логах, какие значения при каких операциях попадают в поток
            .map(it -> it * it)
            // Пример flatMap'а, когда один Publisher мы конвертируем в другой
            .flatMap(it -> Flux.fromIterable(List.of(it, it + 1, it + 2)))
            .filter(it -> it % 3 == 0)
            // Пример concatMap'а, когда один Publisher мы конвертируем в другой
            .concatMap(it -> // Note: здесь можно поменять concatMap на flatMap и наблюдать за вакханалией
                Mono.fromCallable(() -> {
                        log.info("[Mono] Получено значение: {}", it);
                        return it;
                    })
                    .delayElement(Duration.ofSeconds(1)))
            .doOnNext(it -> log.info("[Flux] Получено значение: {}", it))
            .takeWhile(it -> it <= 100)
            .blockLast();
        log.info("Последний квадрат числа, меньше 100 и кратный 3: {}", firstValueUnderHundred);
    }

    @Test
    @DisplayName("Различные стратегии обработки ошибок")
    void test2() {
        var flux = getCounterFlux();

        log.info("Пример с onErrorMap");
        try {
            flux
                .contextWrite(Context.of(TEST_DESCRIPTION_KEY, "onErrorMap"))
                .onErrorMap(it -> {
                    log.error("Ошибка в потоке: {}", it.getMessage());
                    return new IllegalStateException("Ошибка в потоке!");
                }).blockLast();
        } catch (Exception e) {
            log.warn("Ошибка в примере с onErrorMap: {}", e.getMessage());
        }

        log.info("Пример с onErrorResume");
        try {
            var value = flux
                .contextWrite(Context.of(TEST_DESCRIPTION_KEY, "onErrorResume"))
                .onErrorResume(
                    IllegalStateException.class,
                    it -> {
                        log.error("Ошибка в потоке: {}", it.getMessage());
                        return Mono.just(100);
                    })
                .blockLast();
            log.info("Результат исполнения примера с onErrorResume: {}", value);
        } catch (Exception e) {
            log.warn("Ошибка в примере с onErrorResume: {}", e.getMessage());
        }

        log.info("Пример с onErrorComplete");
        try {
            var value = flux
                .contextWrite(Context.of(TEST_DESCRIPTION_KEY, "onErrorComplete"))
                .onErrorComplete(
                    it -> {
                        log.error("Ошибка в потоке: {}", it.getMessage());
                        return true;
                    })
                .blockLast();
            log.info("Результат исполнения примера с onErrorComplete: {}", value);
        } catch (Exception e) {
            log.warn("Ошибка в примере с onErrorComplete: {}", e.getMessage());
        }

        log.info("Пример с onErrorReturn");
        try {
            var value = flux
                .contextWrite(Context.of(TEST_DESCRIPTION_KEY, "onErrorReturn"))
                .onErrorReturn(101)
                .blockLast();
            log.info("Результат исполнения примера с onErrorReturn: {}", value);
        } catch (Exception e) {
            log.warn("Ошибка в примере с onErrorReturn: {}", e.getMessage());
        }

        log.info("Пример с onErrorContinue");
        try {
            var value = Flux.fromStream(IntStream.range(0, 30).boxed())
                .doOnNext(it -> log.info("[onErrorContinue] Состояние счетчика: {}", it))
                .doOnNext(it -> {
                    if (it == 10) {
                        throw new IllegalStateException("Ошибка в потоке!");
                    }
                })
                // Note: поддерживается только рядом операторов, использовать с осторожностью
                .onErrorContinue(
                    IllegalStateException.class,
                    (ex, el) -> log.error("Ошибка {} в потоке при обработке элемента {}", ex.getMessage(), el))
                .blockLast();
            log.info("Результат исполнения примера с onErrorContinue: {}", value);
        } catch (Exception e) {
            log.warn("Ошибка в примере с onErrorContinue: {}", e.getMessage());
        }
    }

    @Test
    @DisplayName("Отличие concatMap от flatMap и backpressure из коробки")
    void test3() {
        // Пример с flatMap, где вышестоящий поток спамит в нижележащий поток,
        // несмотря на то, что он не готов к новым элементам
        log.info("Пример с flatMap");
        Flux.fromStream(IntStream.range(0, 10).boxed())
            .delayElements(Duration.ofMillis(200))
            .flatMap(it ->
                Mono.fromCallable(() -> {
                    log.info("Отправляем в очередь элемент {}", it);
                    return it;
                }).delayElement(Duration.ofSeconds(1))
            )
            .doOnNext(it -> log.info("Получен элемент {}", it))
            .blockLast();

        // Пример с concatMap, который решает эту проблему
        log.info("Пример с concatMap");
        Flux.fromStream(IntStream.range(0, 10).boxed())
            .delayElements(Duration.ofMillis(500))
            .onBackpressureBuffer(100) // todo: рассмотреть разные стратегии
            .concatMap(it ->
                Mono.fromCallable(() -> {
                    log.info("Отправляем в очередь элемент {}", it);
                    return it;
                }).delayElement(Duration.ofSeconds(1))
            )
            .doOnNext(it -> log.info("Получен элемент {}", it))
            .blockLast();
    }

    @Test
    @DisplayName("Планировщики потоков и операции publishOn / subscribeOn")
    void test4() {
        log.info("Пример с publishOn");
        Flux.fromStream(IntStream.range(0, 10).boxed())
            .doOnNext(it -> log.info("[0] Получен элемент {}", it))
            .publishOn(Schedulers.newSingle("publisher-1"))
            .doOnNext(it -> log.info("[1] Получен элемент {}", it))
            .publishOn(Schedulers.newSingle("publisher-2"))
            .doOnNext(it -> log.info("[2] Получен элемент {}", it))
            .subscribeOn(Schedulers.newSingle("subscriber"))
            .blockLast();
    }

    @Test
    @DisplayName("Объединение и параллелизация потоков")
    void test5() {
        // Note: можно рассказать про тестирование Project Reactor с помощью StepVerifier
        log.info("Пример с Mono#zip");
        StepVerifier.create(
            Mono.zip(
                Mono.fromCallable(() -> {
                        log.info("Генерируем первый элемент");
                        return 30;
                    }).delayElement(Duration.ofSeconds(2))
                    .doOnNext(it -> log.info("Получен первый элемент {}", it)),
                Mono.fromCallable(() -> {
                        log.info("Генерируем второй элемент");
                        return 101;
                    }).delayElement(Duration.ofSeconds(1))
                    .doOnNext(it -> log.info("Получен второй элемент {}", it))
                // Mono.empty()
            )
        ).assertNext(it -> {
            int first = it.getT1(), second = it.getT2();
            assertEquals(30, first);
            assertEquals(101, second);
        }).verifyComplete();

        log.info("Пример с Flux#zip");
        Flux.zip(
                Flux.fromStream(IntStream.range(0, 10).boxed())
                    .delayElements(Duration.ofMillis(100))
                    .doOnNext(it -> log.info("Возвращаем элемент из первого источника: {}", it)),
                Flux.fromStream(IntStream.range(11, 30).boxed())
                    .delayElements(Duration.ofMillis(500))
                    .doOnNext(it -> log.info("Возвращаем элемент из второго источника: {}", it))
            ).doOnNext(it -> log.info("Получен элемент: {}", it))
            .blockLast();

        log.info("Пример с Flux#merge");
        Flux.merge(
                Flux.fromStream(IntStream.range(0, 10).boxed())
                    .delayElements(Duration.ofMillis(100))
                    .doOnNext(it -> log.info("Возвращаем элемент из первого источника: {}", it)),
                Flux.fromStream(IntStream.range(11, 30).boxed())
                    .delayElements(Duration.ofMillis(500))
                    .doOnNext(it -> log.info("Возвращаем элемент из второго источника: {}", it))
            ).doOnNext(it -> log.info("Получен элемент: {}", it))
            .blockLast();

        log.info("Пример с Flux#concat");
        Flux.concat(
                Flux.fromStream(IntStream.range(0, 10).boxed())
                    .delayElements(Duration.ofMillis(100))
                    .doOnNext(it -> log.info("Возвращаем элемент из первого источника: {}", it)),
                Flux.fromStream(IntStream.range(11, 30).boxed())
                    .delayElements(Duration.ofMillis(500))
                    .doOnNext(it -> log.info("Возвращаем элемент из второго источника: {}", it))
            ).doOnNext(it -> log.info("Получен элемент: {}", it))
            .blockLast();
    }

    @Test
    @DisplayName("Дебаг, логгирование и отслеживание ошибок")
    void test6() {
        try {
            getCounterFlux().take(30)
                //.log()
                .contextWrite(Context.of(TEST_DESCRIPTION_KEY, "debug"))
                .blockLast();
        } catch (Exception e) {
            // Note: страшный stack-trace
            e.printStackTrace();
        }
    }

    private static Flux<Integer> getCounterFlux() {
        return Flux.<Integer>create(sink -> {
                // todo: рассказать про контекст
                var test = sink.contextView().<String>get(TEST_DESCRIPTION_KEY);
                var state = new AtomicInteger();
                var isCancelled = new AtomicBoolean();
                sink.onDispose(() -> {
                    isCancelled.set(true);
                    log.info("[{}] Поток был завершен!", test);
                });
                sink.onRequest(requested -> {
                    log.info(
                        "[{}] Запрошено {} элементов. Текущее состояние счетчика: {}.",
                        test, requested, state.get());

                    var provided = requested;
                    while (!isCancelled.get() && provided-- > 0) {
                        log.info("[{}] Состояние счетчика: {}", test, state);
                        if (state.get() == 10) {
                            log.error("[{}] Превышен лимит, инициируем ошибку!", test);
                            sink.error(new IllegalStateException("Превышен лимит!"));
                        }
                        sink.next(state.getAndIncrement());
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    log.info("[{}] Завершаем работу потока!", test);
                });
            })
            .subscribeOn(Schedulers.newSingle("counter-generator"));
    }

}
