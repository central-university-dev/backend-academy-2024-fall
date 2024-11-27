package backend.academy.seminar13;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
public class ReactiveStreamsTest {

    private static final Random RANDOM = new Random();

    @Test
    @SneakyThrows
    @DisplayName("Реализация собственного subscriber'а")
    void test0() {
        Flux.fromStream(IntStream.range(0, 1_000).boxed())
            .flatMap(it ->
                Mono.just(it)
                    .delayElement(Duration.ofMillis(it * 10 + RANDOM.nextInt(10, 50))))
            .doOnNext(it -> {
                if (it == 30) {
                    throw new UnsupportedOperationException("Не умею обрабатывать число 30!");
                }
            })
            .subscribe(new CustomSubscriber());

        Thread.sleep(5_000);
    }

    @Test
    @SneakyThrows
    @DisplayName("Реализация собственного publisher'а и subscriber'а")
    void test1() {
        var publisher = Flux.from(new CustomPublisher());
        try (var executor = Executors.newFixedThreadPool(3)) {
            var tasks = IntStream.range(0, 3)
                .mapToObj(ignored -> (Callable<Void>) () -> {
                    publisher
                        .take(10)
                        .subscribe(new CustomSubscriber());
                    return null;
                }).toList();
            executor.invokeAll(tasks);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Реализация собственного оператора")
    void test2() {
        var publisher = Flux.from(new CustomPublisher());
        try (var executor = Executors.newFixedThreadPool(3)) {
            var tasks = IntStream.range(0, 3)
                .mapToObj(ignored -> (Callable<Void>) () -> {
                    Flux.from(new CustomOperator(publisher, it -> it * it))
                        .take(10)
                        .subscribe(new CustomSubscriber());
                    return null;
                }).toList();
            executor.invokeAll(tasks);
        }
    }

    private static class CustomSubscriber implements Subscriber<Integer> {

        private final AtomicReference<Subscription> subscription = new AtomicReference<>();

        @Override
        public void onSubscribe(Subscription s) {
            subscription.set(s);
            subscription.get().request(100);
        }

        @Override
        public void onNext(Integer integer) {
            log.info("Из потока получен следующий элемент: {}", integer);
        }

        @Override
        public void onError(Throwable t) {
            log.error("В потоке произошла следующая ошибка: {}", t.getMessage(), t);
        }

        @Override
        public void onComplete() {
            log.info("Работа Subscriber'а завершена!");
        }

    }

    private static class CustomPublisher implements Publisher<Integer> {

        private final CopyOnWriteArrayList<Subscription> subscriptions = new CopyOnWriteArrayList<>();
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void subscribe(Subscriber<? super Integer> s) {
            var subscription = new CustomSubscription(s);
            s.onSubscribe(subscription);
        }

        @RequiredArgsConstructor
        private class CustomSubscription implements Subscription {

            private final Subscriber<? super Integer> subscriber;

            @Override
            @SneakyThrows
            public void request(long n) {
                var provider = n;
                while (provider-- > 0) {
                    subscriber.onNext(counter.getAndIncrement());
                    Thread.sleep(100);
                }
            }

            @Override
            public void cancel() {
                subscriptions.remove(this);
                subscriber.onComplete();
            }

        }

    }

    @RequiredArgsConstructor
    private static class CustomOperator implements Subscriber<Integer>, Publisher<Integer>, Subscription {

        private final AtomicReference<Subscriber<? super Integer>> subscriber = new AtomicReference<>();
        private final AtomicReference<Subscription> subscription = new AtomicReference<>();

        private final Publisher<Integer> source;
        private final Function<Integer, Integer> mapper;

        @Override
        public void subscribe(Subscriber<? super Integer> s) {
            subscriber.set(s);
            source.subscribe(this);
        }

        @Override
        public void onSubscribe(Subscription s) {
            subscription.set(s);
            subscriber.get().onSubscribe(this);
        }

        @Override
        public void onNext(Integer integer) {
            subscriber.get().onNext(mapper.apply(integer));
        }

        @Override
        public void onError(Throwable t) {
            subscriber.get().onError(t);
        }

        @Override
        public void onComplete() {
            subscriber.get().onComplete();
        }

        @Override
        public void request(long n) {
            subscription.get().request(n);
        }

        @Override
        public void cancel() {
            subscription.get().cancel();
        }

    }

}
