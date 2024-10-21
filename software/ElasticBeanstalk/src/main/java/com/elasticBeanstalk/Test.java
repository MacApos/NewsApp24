package com.elasticBeanstalk;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Test {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Getter
    @Setter
    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                   "name='" + name + '\'' +
                   ", age=" + age +
                   '}';
        }
    }

    public static void main(String[] args) {
        Set<String> fruits = new HashSet<>();
        Stream.of("apple", "banana", "apple").forEach(fruit-> {
            System.out.println(fruits.add(fruit));
        });

        List<Person> people = List.of(new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Alice", 28));

        List<Person> list = people.stream().filter(distinctByKey(Person::getName)).toList();
        System.out.println(list);
    }
}
