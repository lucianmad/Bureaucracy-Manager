package org.entities;

public class PrintJob {
    private final Customer customer;
    private final Document document;
    private final Counter counter;

    public PrintJob(Customer customer, Document document, Counter counter) {
        this.customer = customer;
        this.document = document;
        this.counter = counter;
    }

    public Customer getCustomer() { return customer; }
    public Document getDocument() { return document; }
    public Counter getCounter() { return counter; }
}
