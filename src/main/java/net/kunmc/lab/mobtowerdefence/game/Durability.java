package net.kunmc.lab.mobtowerdefence.game;

import java.util.HashSet;
import java.util.Set;

class Durability {
    private static final int MIN = 0;
    private double durability;
    private final Set<DurabilityObserver> observers = new HashSet<>();

    Durability(double durability) {
        if (durability < 1) {
            throw new IllegalArgumentException("initial durability must be greater than or equal to 1.");
        }

        this.durability = durability;
    }

    void addObserver(DurabilityObserver observer) {
        observers.add(observer);
    }

    void reduce(double n) {
        durability = Math.max(durability - n, MIN);
        notifyObservers();
    }

    boolean isZero() {
        return durability == MIN;
    }

    double remaining() {
        return durability;
    }

    private void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }
}
