package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop(); // это "заглушка, чтобы скомилировать"
    }
}
