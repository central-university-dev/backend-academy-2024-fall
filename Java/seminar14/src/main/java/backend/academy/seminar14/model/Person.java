package backend.academy.seminar14.model;

import backend.academy.seminar14.annotations.MyRuntimeAnnotation;

@MyRuntimeAnnotation(metainfo = "Person metainformation")
public record Person(String name, String surname, int age) {
    private void sayHelloTo(String personName) {
        System.out.printf("Hello, %s! Nice to meet you! My name is %s %s", personName, name, surname);
    }
}
