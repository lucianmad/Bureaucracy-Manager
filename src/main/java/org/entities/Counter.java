package org.entities;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class Counter implements Runnable {
    private final int id;
    private final BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
    private final Document issuedDocument;
    private Map<Document, ArrayList<Document>> docDependencies;
    private final ArrayList<Document> requiredDocuments;
    private Office office;
    private volatile boolean open = true;
    private Thread thread;

    public Counter(int id, Document issuedDocument, Map<Document, ArrayList<Document>> docDependencies) {
        this.id = id;
        this.issuedDocument = issuedDocument;
        this.docDependencies = docDependencies;

        if(docDependencies.get(issuedDocument) != null)
            this.requiredDocuments = docDependencies.get(issuedDocument);
        else
            this.requiredDocuments = new ArrayList<>();
    }

    public void run() {
        try {
            while (true) {
                Customer customer = queue.take();

                while (!open) {
                    Thread.sleep(500);
                }

                if (!checkRequiredDocuments(customer)) {
                    System.out.println("[Counter " + id + "] " + customer.getName() +
                            " is missing required documents for " + issuedDocument + ". Sent back!");
                }

                System.out.println("Counter " + id + " serving " + customer.getName() +
                        " for " + issuedDocument);

                Thread.sleep(1000);
                emitDocs(customer);
            }
        } catch (InterruptedException e) {
            System.out.println("Counter " + id + " closed.");
            Thread.currentThread().interrupt();
        }
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public void addCustomer(Customer customer) {
        try {
            queue.put(customer);
        } catch (InterruptedException e) {
            System.out.println("Counter thread interrupted");
        }
    }

    public boolean checkRequiredDocuments(Customer customer) {
        for (Document requiredDocument : requiredDocuments) {
            if (!customer.hasDocument(requiredDocument))
            {
                System.out.println("MISSING " + requiredDocument + " for " + issuedDocument);
                return false;
            }

        }
        return true;
    }

    public void emitDocs(Customer customer) {
        customer.completeDocument(issuedDocument);

        System.out.println("Counter " + id + " issued " + issuedDocument.toString() +
                " to " + customer.getName());
    }

    public void takeRandomCoffeeBreak() {
        CompletableFuture.runAsync(() -> {
            try {
                open = false;
                System.out.println("Counter " + id + " is taking a coffee break");
                Thread.sleep(3000); // break duration
                open = true;
                System.out.println("Counter " + id + " is back from coffee break");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void usePrinter(){

    }

    public void open(){
        thread = new Thread(this);
        thread.start();
        System.out.println("Counter " + id + " opened.");
    }

    public void close(){
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public int getId() {
        return id;
    }
}
