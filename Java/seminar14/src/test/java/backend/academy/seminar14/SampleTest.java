package backend.academy.seminar14;

import backend.academy.seminar14.annotations.MyRuntimeAnnotation;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

class SampleTest {
    private static final Logger log = LogManager.getLogger(SampleTest.class);
    private static final org.slf4j.Logger log2 = LoggerFactory.getLogger(SampleTest.class);

    public static record Person(String name, String surname, int age) {
    }

    @SneakyThrows
    @Test
    void createObject() {
        Class<?> clazz = Class.forName("backend.academy.seminar14.model.Person");
        // Тут стоит упомянуть про Boxing / Unboxing
        Constructor<?> ctor = clazz.getConstructor(String.class, String.class, int.class);
        Object o = ctor.newInstance("Aleks", "Korotkov", 30);
        System.out.println(o);
    }

    @SneakyThrows
    @Test
    void listMetadata() {
        Class<?> clazz = Class.forName("backend.academy.seminar14.model.Person");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.printf("Method: %s\n", method);
        }
        if (clazz.isRecord()) {
            for (RecordComponent component : clazz.getRecordComponents()) {
                System.out.printf("Record field: %s\n", component);
            }
        } else {
            for (Field field : clazz.getFields()) {
                System.out.printf("Class field: %s\n", field);
            }
        }
    }

    @SneakyThrows
    @Test
    void invokeMethod() {
        var person = new Person("Aleks", "Korotkov", 30);
        Class<?> clazz = Class.forName("backend.academy.seminar14.SampleTest$Person");
        Method method = clazz.getMethod("surname");
        System.out.println(method.invoke(person));
    }

    @SneakyThrows
    @Test
    void invokePrivateMethod() {
        Class<?> clazz = Class.forName("backend.academy.seminar14.model.Person"); //another packet
        Constructor<?> ctor = clazz.getConstructor(String.class, String.class, int.class);
        Object person = ctor.newInstance("Aleks", "Korotkov", 30);
        Assertions.assertThrows(NoSuchMethodException.class, () -> clazz.getMethod("sayHelloTo", String.class));
        Method declaredMethod = clazz.getDeclaredMethod("sayHelloTo", String.class);
        Assertions.assertThrows(IllegalAccessException.class, () -> declaredMethod.invoke(person, "Maxim"));
        declaredMethod.trySetAccessible(); // suppress the JVM access control checks.
        declaredMethod.invoke(person, "Oleg");
    }

    @SneakyThrows
    @Test
    void annotationTest() {
        Class<?> clazz = Class.forName("backend.academy.seminar14.model.Person"); //another packet
        if (clazz.isAnnotationPresent(MyRuntimeAnnotation.class)) {
            Assertions.assertEquals("Person metainformation",
                clazz.getAnnotation(MyRuntimeAnnotation.class).metainfo());
        }
    }

}
