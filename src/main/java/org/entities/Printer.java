package org.entities;

import java.util.concurrent.Semaphore;

public class Printer  {
    private final Semaphore printerLock = new Semaphore(1, true);
    private String name;

    public Printer(String name) {
        this.name = name;
    }

    public void print(Document document, Counter counter, Customer customer) {
        try {
            printerLock.acquire();
            System.out.println("[" + name + "] Printing " + document +
                    " for " + customer.getName() + " from Counter " + counter.getId());
            Thread.sleep(1000);
            System.out.println("[" + name + "] Done printing " + document);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            printerLock.release();
        }
    }
}
