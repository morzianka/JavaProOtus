package ru.otus.pro;

/**
 * @author Shabunina Anita
 */
public class Calculator {

    @Log
    public void calculation(int param1) {
        System.out.printf("Calculator: param1 + 5 = %s%n", param1 + 5);
    }

    public void calculation(int param1, int param2) {
        System.out.printf("Calculator: param1 + param2 = %s%n", param1 + param2);
    }

    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.printf("Calculator: param1 + param2 = %s, param3 = %s%n", param1 + param2, param3);
    }
}
