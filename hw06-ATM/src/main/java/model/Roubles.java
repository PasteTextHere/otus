package model;

import interfaces.Banknotes;

public enum Roubles implements Banknotes {
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private final int banknoteValue;

    Roubles(int value) {
        this.banknoteValue = value;
    }

    @Override
    public int getBanknoteValue() {
        return this.banknoteValue;
    }
}
