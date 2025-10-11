package org.entities;

import java.util.HashSet;
import java.util.Set;

public class Customer {
    private String name;
    private Set<Document> ownedDocuments = new HashSet<>();
    private Document goalDocument;

    public Customer(String name, Document goal) {
        this.name = name;
        this.goalDocument = goal;
    }

    public String getName() {
        return name;
    }

    public void receiveDocument(Document doc) {
        ownedDocuments.add(doc);
    }

    public boolean hasDocument(Document doc) {
        return ownedDocuments.contains(doc);
    }
}
