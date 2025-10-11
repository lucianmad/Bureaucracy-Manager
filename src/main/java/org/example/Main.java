package org.example;

import org.entities.Counter;
import org.entities.Customer;
import org.entities.Document;
import org.entities.Office;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var taxReceipt = new Document("Tax Receipt");
        var idCopy = new Document("Id Copy");
        var certificate = new Document("Certificate");

        var taxOffice = new Office("Tax Office", taxReceipt);
        taxOffice.addCounter(new Counter(1, null));
        taxOffice.addCounter(new Counter(2, null));
        taxOffice.addCounter(new Counter(3, null));

        var idCopyOffice = new Office("Id Copy Office", idCopy);
        idCopyOffice.addCounter(new Counter(4, taxReceipt));
        idCopyOffice.addCounter(new Counter(5, taxReceipt));
        idCopyOffice.addCounter(new Counter(6, taxReceipt));

        var certificateOffice = new Office("Certificate Office", certificate);
        certificateOffice.addCounter(new Counter(7, idCopy));
        certificateOffice.addCounter(new Counter(8, idCopy));

        var customer = new Customer("Customer 1", certificate);

        taxOffice.addCustomer(customer);
        var taxOfficeCounter = taxOffice.getCounters().getFirst();
        taxOfficeCounter.addCustomer(customer);
        taxOfficeCounter.emitDocs(customer);

        idCopyOffice.addCustomer(customer);
        var idCopyOfficeCounter = idCopyOffice.getCounters().getFirst();
        idCopyOfficeCounter.addCustomer(customer);
        idCopyOfficeCounter.emitDocs(customer);

        certificateOffice.addCustomer(customer);
        var certificateOfficeCounter = certificateOffice.getCounters().getFirst();
        certificateOfficeCounter.addCustomer(customer);
        certificateOfficeCounter.emitDocs(customer);

        var customer2 = new Customer("Customer 2", certificate);

        idCopyOffice.addCustomer(customer2);
        var idCopyOfficeCounter2 = idCopyOffice.getCounters().getFirst();
        idCopyOfficeCounter2.addCustomer(customer2);
        idCopyOfficeCounter2.emitDocs(customer2);
    }
}