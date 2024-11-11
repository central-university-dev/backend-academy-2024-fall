package backend.academy.seminar9.assertj;

import java.util.List;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class AssertjExampleTest implements WithAssertions {

    @Test
    void assertjAssertionsTest() {
        String hello = "hello world";

        assertThat(hello)
            .isEqualTo("hello world");

        assertThat(hello)
            .startsWith("hello");

        String digits = "123-456-7890";

        assertThat(digits)
            .matches("\\d{3}-\\d{3}-\\d{4}");

        List<String> fruits = List.of("apple", "banana");

        assertThat(fruits)
            .containsExactlyInAnyOrder("banana", "apple");

        assertThat(fruits)
            .hasSize(2);
    }

    private record Address(String city, String postalCode) {
    }

    private record Person(String name, int age, Address address) {
    }

    @Test
    void objectChainedAssertionsTest() {
        Address address = new Address("Moscow", "12345");
        Person person = new Person("Ivan Petrov", 30, address);

        assertThat(person)
            .isNotNull()
            .extracting("name", "age")
            .containsExactly("Ivan Petrov", 30);

        assertThat(person)
            .extracting(Person::address) // type-safe
            .isNotNull()
            .extracting(Address::city, Address::postalCode)
            .containsExactly("Moscow", "12345");
    }

    @Test
    void collectionsChainedAssertionsTest() {
        Address address1 = new Address("Moscow", "12345");
        Address address2 = new Address("Saint-Petersburg", "54321");

        Person person1 = new Person("Ivan Petrov", 30, address1);
        Person person2 = new Person("Petr Ivanov", 25, address2);
        Person person3 = new Person("Egor Smirnov", 40, address2);

        List<Person> persons = List.of(person1, person2, person3);

        assertThat(persons)
            .hasSize(3)
            .filteredOn(person -> person.age() > 30)
            .singleElement()
            .extracting(Person::name)
            .isEqualTo("Egor Smirnov");

        assertThat(persons)
            .extracting(Person::address)
            .extracting(Address::postalCode)
            .containsOnly("54321", "12345");
    }

    @Test
    void exceptionsAssertionsTest() {
        assertThatThrownBy(() -> {
            throw new NumberFormatException("Some error!");
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("error!")
            .hasNoCause();

        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Ooops!", new NullPointerException("Null!"));
        })
            .hasRootCauseInstanceOf(NullPointerException.class)
            .hasRootCauseMessage("Null!");
    }
}
