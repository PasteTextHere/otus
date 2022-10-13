package homework;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> customerList = new ArrayDeque<>();

    public void add(Customer customer) {
        customerList.addLast(customer);
    }

    public Customer take() {
        return customerList.pollLast();
    }
}
