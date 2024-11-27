package backend.academy.seminar12.sync;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
public class SyncTest extends AbstractConcurrentTest {

    @Test
    @SneakyThrows
    @DisplayName("Примеры использования синхронизаторов - CountDownLatch")
    void test0() {
        record Fio(String name, String surname) {

        }

        record PaymentData(String cardNumber, String cvv) {

        }

        @Data
        @Accessors(chain = true)
        class Client {

            private final UUID id;

            // Note: заполняются походом в смежные системы
            private PaymentData paymentData;
            private Fio fio;

        }

        var client = new Client(UUID.randomUUID());
        var latch = new CountDownLatch(2);

        startThread(() -> {
            while (!latch.await(1000, TimeUnit.MILLISECONDS)) {
                log.info("Все еще ждем, пока будут заполнены все данные о клиенте...");
            }

            log.info("Все данные о клиенте заполнены: {}", client);
        });

        startThread(() -> {
            log.info("Эмулируем поход в смежную систему за ФИО пользователя");
            Thread.sleep(5_000);

            log.info("Получили ФИО пользователя!");
            client.setFio(new Fio("Sergey", "Khvatov"));
            latch.countDown();
        });

        startThread(() -> {
            log.info("Эмулируем поход в смежную систему за платежной информацией пользователя");
            Thread.sleep(2_000);

            log.info("Получили платежную информацию пользователя!");
            client.setPaymentData(new PaymentData("1234 xxxx 5678", "000"));
            latch.countDown();
        });

        Thread.sleep(6_000);
    }

    @Test
    @SneakyThrows
    @DisplayName("Примеры использования синхронизаторов - Exchanger")
    void test1() {
        var exchanger = new Exchanger<String>();

        startThread(() -> {
            log.info("Данный поток ожидаем секретного сообщения для обмена...");
            var message = exchanger.exchange("Wow! So secure!", 1_000, TimeUnit.MILLISECONDS);
            log.info("Полученное в ответ сообщение: {}", message);
        });

        startThread(() -> {
            log.info("Данный поток генерирует секретное сообщение для обмена...");
            Thread.sleep(500);

            var secretMessage = "Very Secret Message".replace("e", "X");
            var message = exchanger.exchange(secretMessage, 1_000, TimeUnit.MILLISECONDS);
            log.info("Полученное в ответ сообщение: {}", message);
        });

        Thread.sleep(2_000);
    }

    @Test
    @SneakyThrows
    @DisplayName("Примеры использования синхронизаторов - ReentrantLock")
    void test2() {
        var lock = new ReentrantReadWriteLock();

        final var nonAtomicCounter = new Object() {
            private int value = 0;

            public int value() {
                return value;
            }

            @SneakyThrows
            public void increment() {
                Thread.yield();
                value++;
            }
        };

        IntStream.range(0, 3).forEach(it ->
            startThread(() -> {
                log.info("Поток {} только читает значение счетчика!", currentThreadName());

                var readLock = lock.readLock();
                while (!currentThread().isInterrupted()) {
                    while (!readLock.tryLock(300, TimeUnit.MILLISECONDS)) {
                        log.info("Возможность чтения все еще заблокирована!");
                    }

                    log.info("Значение счетчика: {}", nonAtomicCounter.value());
                    Thread.sleep(200);
                    readLock.unlock();
                }
            }));

        startThread(() -> {
            log.info("Поток {} только и читает, и ПИШЕТ значение счетчика!", currentThreadName());

            var writeLock = lock.writeLock();
            while (!currentThread().isInterrupted()) {
                while (!writeLock.tryLock(300, TimeUnit.MILLISECONDS)) {
                    log.info("Возможность записи все еще заблокирована, так как есть читатели!");
                }

                log.info("Увеличиваем значение счетчика!");
                nonAtomicCounter.increment();
                Thread.sleep(500);

                log.info("Значение счетчика после увеличения: {}", nonAtomicCounter.value());
                writeLock.unlock();
            }
        });

        Thread.sleep(2_000);
    }

}
