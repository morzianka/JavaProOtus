package ru.otus.pro;


/*
    java -javaagent:proxyDemo-0.0.1.jar -jar proxyDemo-0.0.1.jar
    javap -c Name.class //byte code of a class
*/
public class ProxyDemo {

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        calc.calculation(1);
        calc.calculation(2, 3);
        calc.calculation(4, 5, "text param");
    }
}
