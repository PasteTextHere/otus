package homework;


import java.util.*;
import java.util.Map.Entry;

public class CustomerService {

    private final Map<Customer, String> customerMap = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Entry<Customer, String> getSmallest() {
        Entry<Customer, String> smallest = customerMap.entrySet().stream().findFirst().orElse(null);
        try {
            return new AbstractMap.SimpleEntry<>(new Customer(smallest.getKey()), smallest.getValue());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Entry<Customer, String> getNext(Customer customer) {
        Set<Entry<Customer, String>> entryList = customerMap.entrySet();
        Optional<Entry<Customer, String>> tmp = entryList.stream()
                .filter(entry -> entry.getKey().getScores() > customer.getScores())
                .findFirst();
        if (tmp.isPresent()) {
            return new AbstractMap.SimpleEntry<>(new Customer(tmp.get().getKey()), tmp.get().getValue());
        } else return null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
