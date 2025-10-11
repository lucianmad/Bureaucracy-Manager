package org.entities;

public class Document {
    private final String name;

    public Document(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Document{" + name + '}';
    }
}

