package org.example;

import org.entities.Counter;
import org.entities.Customer;
import org.entities.Document;
import org.entities.Office;

import java.net.CookieHandler;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        var taxReceipt = new Document("Tax Receipt");
        var idCopy = new Document("Id Copy");
        var medicalReport = new Document("Medical Report");
        var certificate = new Document("Certificate");

        var Office1 = new Office("Office 1");
        var Office2 = new Office("Office 2");
        var Office3 = new Office("Office 3");

        Map<Document, ArrayList<Document>> documentDependencies = new HashMap<>();

        documentDependencies.put(medicalReport, new ArrayList<>(List.of(taxReceipt, idCopy)));
        documentDependencies.put(certificate, new ArrayList<>(List.of(taxReceipt, idCopy, medicalReport)));


        var Counter1 = new Counter(1, idCopy, documentDependencies);
        var Counter2 = new Counter(2, idCopy, documentDependencies);
        var Counter3 = new Counter(3, taxReceipt, documentDependencies);
        var Counter4 = new Counter(4, idCopy, documentDependencies);
        var Counter5 = new Counter(5, taxReceipt, documentDependencies);
        var Counter6 = new Counter(6, medicalReport, documentDependencies);
        var Counter7 = new Counter(7, medicalReport, documentDependencies);
        var Counter8 = new Counter(8, certificate, documentDependencies);

        Office1.addCounter(Counter1);
        Office1.addCounter(Counter2);
        Office1.addCounter(Counter3);
        Office2.addCounter(Counter4);
        Office2.addCounter(Counter5);
        Office2.addCounter(Counter6);
        Office3.addCounter(Counter7);
        Office3.addCounter(Counter8);


        Map<Document, ArrayList<Counter>> obtainDocumentFromCounters = new HashMap<>();

        obtainDocumentFromCounters.put(taxReceipt, new ArrayList<>(List.of(Counter3, Counter5)));
        obtainDocumentFromCounters.put(idCopy, new ArrayList<>(List.of(Counter1, Counter2, Counter4)));
        obtainDocumentFromCounters.put(medicalReport, new ArrayList<>(List.of(Counter6, Counter7)));
        obtainDocumentFromCounters.put(certificate, new ArrayList<>(List.of(Counter8)));

        var Customer1 = new Customer(
                "Elvis",
                certificate,
                documentDependencies,
                obtainDocumentFromCounters,
                new ArrayList<>(List.of(taxReceipt))
        );

        var Customer2 = new Customer(
                "Dorian",
                medicalReport,
                documentDependencies,
                obtainDocumentFromCounters
        );



        int numCounters = 8;
        int numCustomers = 2;

        ExecutorService executor = Executors.newFixedThreadPool(numCounters + numCustomers);

        List<Counter> allCounters = List.of(Counter1, Counter2, Counter3, Counter4, Counter5, Counter6, Counter7, Counter8);
        for (Counter counter : allCounters) {
            executor.submit(counter);
        }

        executor.submit(Customer1);
        executor.submit(Customer2);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Counter randomCounter = allCounters.get(new Random().nextInt(allCounters.size()));
            if (Math.random() < 0.5) randomCounter.takeRandomCoffeeBreak();
        }, 2, 5, TimeUnit.SECONDS);

        // --- End simulation after 30s
        CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
            System.out.println("Simulation ended.");
            scheduler.shutdownNow();
            executor.shutdownNow();
        });

    }
}