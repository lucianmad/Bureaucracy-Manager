package org.entities;

import java.util.*;

public class Office {
    private final String name;
    private final ArrayList<Counter> counters = new ArrayList<>();
    private final Printer printer;

    public Office(String name, Printer printer) {
        this.name = name;
        this.printer = printer;
    }

    public void addCounter(Counter counter) {
        counters.add(counter);
        counter.setOffice(this);
    }

    public String getName() {
        return name;
    }

    public Printer getPrinter() {
        return printer;
    }
}
