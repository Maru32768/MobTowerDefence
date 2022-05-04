package net.kunmc.lab.mobtowerdefence.game;

class Durability {
    private final int durability;

    Durability(int durability) {
        if (durability < 1) {
            throw new IllegalArgumentException("durability must be greater than or equal to 1.");
        }

        this.durability = durability;
    }

    Durability minus(int n) {
        return new Durability(durability - n);
    }
}
