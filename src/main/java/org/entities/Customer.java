package org.entities;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Customer implements Runnable {
    private final String name;
    private final ArrayList<Document> ownedDocuments;
    private final Document goalDocument;
    private final ArrayList<Document> documentsToObtain;
    private final Map<Document, ArrayList<Counter>> obtainDocumentFromCounters;

    private final Map<Document, CompletableFuture<Document>> pendingDocs = new HashMap<>();

    public Customer(String name, Document goal, Map<Document, ArrayList<Document>> docDependencies, Map<Document, ArrayList<Counter>> docFromCounters) {
        this.name = name;
        this.goalDocument = goal;
        this.documentsToObtain = new ArrayList<>();
        if (docDependencies.get(goal) != null)
            this.documentsToObtain.addAll(docDependencies.get(goal));
        this.documentsToObtain.add(goal);
        this.obtainDocumentFromCounters = docFromCounters;
        ownedDocuments = new ArrayList<>();
    }

    public Customer(String name, Document goal, Map<Document, ArrayList<Document>> docDependencies, Map<Document, ArrayList<Counter>> docFromCounters, ArrayList<Document> ownedDocuments) {
        this.name = name;
        this.goalDocument = goal;
        this.documentsToObtain = new ArrayList<>();
        if (docDependencies.get(goal) != null)
            this.documentsToObtain.addAll(docDependencies.get(goal));
        this.documentsToObtain.add(goal);
        this.obtainDocumentFromCounters = docFromCounters;
        this.ownedDocuments = ownedDocuments;
    }

    private boolean hasAllPrerequisites(Document doc) {
        var required = documentsToObtain.subList(0, documentsToObtain.indexOf(doc));
        return ownedDocuments.containsAll(required);
    }

    public void run() {
        System.out.println(name + " wants to obtain " + goalDocument);

        for (Document doc : documentsToObtain) {
            while (!hasAllPrerequisites(doc)) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Counter counter = getAvailableCounterForDoc(doc);

            CompletableFuture<Document> future = new CompletableFuture<>();
            pendingDocs.put(doc, future);

            goToCounterForDoc(counter, doc);

            try {
                Document obtained = future.get();
                receiveDocument(obtained);
            } catch (Exception e) {
                System.out.println(name + " failed to obtain " + doc + ": " + e.getMessage());
            }
        }
    }

    public Counter getAvailableCounterForDoc(Document doc) {
        ArrayList<Counter> counters = obtainDocumentFromCounters.get(doc);
        Counter counter;
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            List<Counter> availableCounters = counters.stream()
                    .filter(Counter::isOpen)
                    .toList();
            if (!availableCounters.isEmpty()) {
                counter = availableCounters.get(new Random().nextInt(availableCounters.size()));
                break;
            }
            else {
                System.out.println("No counters are currently open for document " + doc + "! Please wait a moment.");
            }
        }
        return counter;
    }

    public void goToCounterForDoc(Counter counter, Document doc) {
        System.out.println(name + " goes to Counter " + counter.getId() + " for " + doc);
        counter.addCustomer(this);
    }

    public void completeDocument(Document doc) {
        CompletableFuture<Document> future = pendingDocs.get(doc);
        if (future != null && !future.isDone()) {
            future.complete(doc);
        }
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
