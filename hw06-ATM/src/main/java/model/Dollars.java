package model;

import interfaces.Banknotes;

public enum Dollars implements Banknotes {
    DOLLAR(150);

    private final int banknoteValue;

    Dollars(int banknoteValue) {
        this.banknoteValue = banknoteValue;
    }

    @Override
    public int getBanknoteValue() {
        return banknoteValue;
    }
}
