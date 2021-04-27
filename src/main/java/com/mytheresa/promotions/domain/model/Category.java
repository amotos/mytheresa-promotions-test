package com.mytheresa.promotions.domain.model;

public enum Category {

    BOOTS("Boots"), SANDALS("Sandals"), SNEAKERS("Sneakers");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
