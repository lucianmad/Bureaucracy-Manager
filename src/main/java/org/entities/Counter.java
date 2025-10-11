package org.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Counter {
    private int id;
    private Office office;
    private BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
    private Document neededDocument;

    public Counter(Office office, int id, Document neededDocument) {
        this.office = office;
        this.id = id;
        this.neededDocument = neededDocument;
    }

    public void emitDocs(Document document, Customer customer) {

    }

    public boolean verifyDocs(Customer customer) {
        return customer.hasDocument(neededDocument);
    }

    public void coffeeBreak() {

    }
}
