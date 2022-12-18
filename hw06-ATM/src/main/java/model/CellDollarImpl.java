package model;

import interfaces.Banknotes;
import interfaces.Cell;

public class CellDollarImpl implements Cell {
    private final Dollars currentBanknote;
    private int banknotesCounter = 0;

    public CellDollarImpl(Dollars currentBanknote) {
        this.currentBanknote = currentBanknote;
    }

    @Override
    public Dollars cellType() {
        return this.currentBanknote;
    }

    @Override
    public void showCellInfo() {
        System.out.printf("Banknote value: %d   Banknote counter: %d\n", currentBanknote.getBanknoteValue(), banknotesCounter);
    }

    @Override
    public void put(Banknotes banknote) {
        banknotesCounter ++;
    }

    @Override
    public void get(Banknotes banknote) {
        banknotesCounter --;
    }

    @Override
    public int getLimit() {
        return banknotesCounter * currentBanknote.getBanknoteValue();
    }

    @Override
    public int getPiecesLost() {
        return banknotesCounter;
    }

    @Override
    public boolean isAvailable() {
        return banknotesCounter > 0;
    }
}
