package org.entities;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Office {
    private String name;
    private Collection<Counter> counters;
    private BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
    //private Document documentIssued;

    public Office(String name, Collection<Counter> counters) {
        this.name = name;
        this.counters = counters;
    }

    public void usePrinter(Counter counter) {

    }

    public String getName() {
        return name;
    }

    public void addCustomer(Customer c) {
        queue.add(c);
    }

    public BlockingQueue<Customer> getQueue() {
        return queue;
    }

    public void openCounters() {

    }

}
