package org.example;

import org.entities.*;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        var taxReceipt = new Document("Tax Receipt");
        var idCopy = new Document("Id Copy");
        var medicalReport = new Document("Medical Report");
        var certificate = new Document("Certificate");

        var printerOffice1 = new Printer("Office 1 Printer");
        var printerOffice2 = new Printer("Office 2 Printer");
        var printerOffice3 = new Printer("Office 3 Printer");

        var Office1 = new Office("Office 1", printerOffice1);
        var Office2 = new Office("Office 2", printerOffice2);
        var Office3 = new Office("Office 3", printerOffice3);

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

        var Customer3 = new Customer(
                "Florin",
                certificate,
                documentDependencies,
                obtainDocumentFromCounters
        );

        var Customer4 = new Customer(
                "Ana",
                medicalReport,
                documentDependencies,
                obtainDocumentFromCounters
        );

        List<Customer> allCustomers = List.of(Customer1, Customer2, Customer3, Customer4);
        List<Counter> allCounters = List.of(Counter1, Counter2, Counter3, Counter4, Counter5, Counter6, Counter7, Counter8);


        ExecutorService executor = Executors.newFixedThreadPool(allCounters.size() + allCustomers.size());

        for (Counter counter : allCounters) {
            executor.submit(counter);
        }


        List<CompletableFuture<Void>> customerFutures = new ArrayList<>();
        for (Customer customer : allCustomers) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(customer, executor);
            customerFutures.add(future);
        }


        Random random = new Random();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            var randomCounter = allCounters.get(random.nextInt(allCounters.size()));
            if (Math.random() < 0.5) {
                CompletableFuture.runAsync(() -> randomCounter.takeRandomCoffeeBreak());
            }
        }, 5, 5, TimeUnit.SECONDS);


        CompletableFuture.allOf(customerFutures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    System.out.println("All customers have received their goal documents!");
                    scheduler.shutdownNow();
                    executor.shutdownNow();
                });
    }
}