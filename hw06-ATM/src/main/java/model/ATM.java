package model;

import interfaces.*;
import model.exception.NotEnoughMoney;
import model.exception.NotValidSum;
import model.exception.UnsupportedBanknote;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class ATM implements Input, Output, Info {

    protected final Map<Banknotes, Cell> abstractCellStorage;

    public ATM(Map<Banknotes, Cell> abstractCellStorage) {
        this.abstractCellStorage = abstractCellStorage;
    }

    @Override
    public void showBalance() {
        var atmAvailableSum = getAvailableCellsAsStream()
                .peek(Cell::showCellInfo)
                .mapToInt(Cell::getLimit)
                .sum();
        System.out.printf("Available sum in %s is %d\n", this.getClass().getSimpleName(), atmAvailableSum);
    }

    @Override
    public void putBanknotes(Map<? extends Banknotes, Integer> inputBanknotes) {
        inputBanknotes.forEach((key, value) -> {
            for (int i = 0; i < value; i++) {
                abstractCellStorage.values().stream()
                        .filter(cell -> Objects.equals(cell.cellType(), key))
                        .findFirst()
                        .orElseThrow(() -> new UnsupportedBanknote("This banknote is unsupported yet"))
                        .put(key);
            }
        });
    }

    @Override
    public void extraditeSum(int inputSum) {
        var availableSum = getAvailableCellsAsStream().mapToInt(Cell::getLimit).sum();
        var minimalBanknoteValue = getAvailableCellsAsStream()
                .min(Comparator.comparingInt(cell -> cell.cellType().getBanknoteValue()))
                .orElseThrow(() -> new NotEnoughMoney("Not enough money. ATM machine is empty"))
                .cellType().getBanknoteValue();

        if (inputSum > availableSum) {
            throw new NotEnoughMoney(String.format("Not enough money. Available sum is %d", availableSum));
        }
        if (inputSum % 50 != 0) {
            throw new NotValidSum("Enter valid sum. At least multiple to 50");
        } else {
            var tmpSum = inputSum;
            Map<Banknotes, Integer> toExtradite = new HashMap<>();
            do {
                for (Cell cell : getAvailableCellsAsStream().toList()) {
                    var neededBanknotes = inputSum / cell.cellType().getBanknoteValue();
                    var banknotesToExtradite = Math.min(neededBanknotes, cell.getPiecesLost());
                    if (banknotesToExtradite > 0) {
                        if (tmpSum == banknotesToExtradite * cell.cellType().getBanknoteValue()) {
                            tmpSum = tmpSum - (banknotesToExtradite * cell.cellType().getBanknoteValue());
                            toExtradite.put(cell.cellType(), banknotesToExtradite);
                            break;
                        } else if (tmpSum > banknotesToExtradite * cell.cellType().getBanknoteValue()) {
                            tmpSum = tmpSum - (banknotesToExtradite * cell.cellType().getBanknoteValue());
                            toExtradite.put(cell.cellType(), banknotesToExtradite);
                        } else {
                            while (tmpSum < banknotesToExtradite * cell.cellType().getBanknoteValue()) {
                                banknotesToExtradite--;
                                if (banknotesToExtradite <= 0) {
                                    break;
                                }
                            }
                            tmpSum = tmpSum - (banknotesToExtradite * cell.cellType().getBanknoteValue());
                            toExtradite.put(cell.cellType(), banknotesToExtradite);
                        }
                    }
                }
            } while (tmpSum > minimalBanknoteValue);

            if (tmpSum == 0) {
                toExtradite.forEach((key, value) -> {
                    for (int i = 0; i < value; i++) {
                        abstractCellStorage.get(key).get(key);
                    }
                });
                StringBuilder payedBanknotesInfo = new StringBuilder();
                for (Map.Entry<Banknotes, Integer> entry : toExtradite.entrySet()) {
                    payedBanknotesInfo.append(String.format("       %s - %s %s\n", entry.getKey().getBanknoteValue(), entry.getValue(), entry.getValue() > 1 ? "times" : "time"));
                }
                System.out.printf("Success payed %d by:\n%s%n", inputSum, payedBanknotesInfo);
            } else {
                throw new NotEnoughMoney("Can't pay this sum by available banknotes");
            }
        }
    }

    private Stream<? extends Cell> getAvailableCellsAsStream() {
        return abstractCellStorage.values().stream()
                .filter(Cell::isAvailable);
    }
}
