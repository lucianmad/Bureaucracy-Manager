package org.entities;

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
    private volatile boolean servingCustomer = false;

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

                while (!open) {
                    Thread.sleep(500);
                }

                Customer customer = queue.take();
                servingCustomer = true;

                if (!checkRequiredDocuments(customer)) {
                    System.out.println(office.getName() + ": [Counter " + id + "] " + customer.getName() +
                            " is missing required documents for " + issuedDocument + ". Sent back!");
                }

                System.out.println(office.getName() + ": Counter " + id + " serving " + customer.getName() +
                        " for " + issuedDocument);

                Thread.sleep(2000);

                usePrinter(customer.getName());
                emitDocs(customer);
                servingCustomer = false;

                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            System.out.println(office.getName() + ": Counter " + id + " closed.");
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

        System.out.println(office.getName() + ": Counter " + id + " issued " + issuedDocument +
                " to " + customer.getName());
    }

    public void takeRandomCoffeeBreak() {
        CompletableFuture.runAsync(() -> {
            try {
                while (servingCustomer) {
                    Thread.sleep(100);
                }

                open = false;
                System.out.println("[COFFEE BREAK] " + office.getName() + ": Counter " + id + " is taking a coffee break");

                ArrayList<Customer> redirected = new ArrayList<>();
                queue.drainTo(redirected);
                for (Customer c : redirected) {
                    Document doc = this.issuedDocument;
                    Counter newCounter = c.getAvailableCounterForDoc(doc);
                    System.out.println(c.getName() + " left Counter " + id);
                    c.goToCounterForDoc(newCounter, doc);
                }

                Thread.sleep(3000);
                open = true;
                System.out.println(office.getName() + ": Counter " + id + " is back from coffee break");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void usePrinter(String customerName){
        office.getPrinter().print(issuedDocument, this, customerName);
    }

    public int getId() {
        return id;
    }

    public boolean isOpen() {
        return  open;
    }
}
