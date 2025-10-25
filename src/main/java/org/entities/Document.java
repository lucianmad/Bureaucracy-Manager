package org.entities;

public record Document(String name) {

    @Override
    public String toString() {
        return "Document '" + name + "'";
    }
}

