package model;

public enum Banknotes {
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);


    private int banknoteCounter;
    private final int banknoteValue;

    Banknotes(int value) {
        this.banknoteValue = value;
        this.banknoteCounter = 0;
    }

    public int getBanknoteValue() {
        return this.banknoteValue;
    }

    public int getBanknoteCounter() {
        return banknoteCounter;
    }

    public void addBanknote() {
        banknoteCounter++;
    }

    public Banknotes payBanknote() {
        if (banknoteCounter < 0) {
            throw new BanknoteEmpty(String.format("Banknotes %s is lost", this.banknoteValue));
        } else {
            banknoteCounter--;
        }
        return this;
    }
}
