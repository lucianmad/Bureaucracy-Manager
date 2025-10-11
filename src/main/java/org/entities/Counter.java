package org.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Counter {
    private final int id;
    private final BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
    private final Document neededDocument;
    private Office office;

    public Counter(int id, Document neededDocument) {
        this.id = id;
        this.neededDocument = neededDocument;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public void addCustomer(Customer customer) {
        queue.add(customer);
    }

    public void emitDocs(Customer customer) {
        if (verifyDocs(customer) || neededDocument == null) {
            System.out.println("Take your document sir!");
            customer.receiveDocument(office.getDocumentIssued());
        }
        else {
            System.out.println("Go to previous office and get your document!");
        }
    }

    private boolean verifyDocs(Customer customer) {
        return customer.hasDocument(neededDocument);
    }

    public void coffeeBreak() {
        //dai sleep la counter pt o perioada de timp
    }

    public void usePrinter(){

    }

    public void open(){
        //create thread
    }

    public void close(){
        //kill thread
    }
}
