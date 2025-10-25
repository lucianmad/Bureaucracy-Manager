package org.entities;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Office {
    private final String name;
    private final ArrayList<Counter> counters = new ArrayList<>();
    private final BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
    private Printer printer;

    public Office(String name) {
        this.name = name;
    }

    public void addCounter(Counter counter) {
        counters.add(counter);
        counter.setOffice(this);
    }

    public String getName() {
        return name;
    }

    public BlockingQueue<Customer> getQueue() {
        return queue;
    }

    public void addCustomer(Customer c) {
        queue.add(c);
    }

    public ArrayList<Counter> getCounters() {
        return counters;
    }

    public void openCounter(Counter counter) {
        counter.open();
    }

    public void closeCounter(Counter counter) {
        counter.close();
    }
}
