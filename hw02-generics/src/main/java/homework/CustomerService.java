package homework;

import java.util.*;

public class CustomerService {

    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> immutableEntry = customers.firstEntry();
        return new AbstractMap.SimpleImmutableEntry<>(
                new Customer(immutableEntry.getKey()), immutableEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Customer next = customers.higherKey(customer);
        return next == null ? null : new AbstractMap.SimpleImmutableEntry<>(new Customer(next), customers.get(next));
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
