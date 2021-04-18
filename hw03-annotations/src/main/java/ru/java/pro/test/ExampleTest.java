package ru.java.pro.test;

import ru.java.pro.annotation.After;
import ru.java.pro.annotation.Before;
import ru.java.pro.annotation.Test;

/**
 * @author Shabunina Anita
 */
public class ExampleTest {

    private static int counter = 1;

    @Before
    public void init() {
        System.out.println("Before " + counter);
    }

    @Test
    public void test1() {
        System.out.println("Test_1");
    }

    @Test
    public void test2() {
        System.out.println("Test_2");
        throw new RuntimeException();
    }

    @Test
    public void test3() {
        System.out.println("Test_3");
    }

    @After
    public void clean() {
        System.out.println(("After " + counter++));
    }
}
