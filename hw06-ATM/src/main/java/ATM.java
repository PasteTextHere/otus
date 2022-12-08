import input.AnyInputImpl;
import model.Banknotes;
import model.NotEnoughMoney;
import model.NotValidSum;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.Banknotes.*;

public abstract class ATM implements AnyInputImpl {

    public int getTotalCash() {
        return FIFTY.getBanknoteCounter() * FIFTY.getBanknoteValue() +
                ONE_HUNDRED.getBanknoteCounter() * ONE_HUNDRED.getBanknoteValue() +
                TWO_HUNDRED.getBanknoteCounter() * TWO_HUNDRED.getBanknoteValue() +
                FIVE_HUNDRED.getBanknoteCounter() * FIVE_HUNDRED.getBanknoteValue() +
                ONE_THOUSAND.getBanknoteCounter() * ONE_THOUSAND.getBanknoteValue() +
                TWO_THOUSAND.getBanknoteCounter() * TWO_THOUSAND.getBanknoteValue() +
                FIVE_THOUSAND.getBanknoteCounter() * FIVE_THOUSAND.getBanknoteValue();
    }

    public void putFifty(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            FIFTY.addBanknote();
        }
        printInput(FIFTY, banknoteCount);
    }

    public void putOneHundred(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            ONE_HUNDRED.addBanknote();
        }
        printInput(ONE_HUNDRED, banknoteCount);
    }

    public void putTwoHundred(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            TWO_HUNDRED.addBanknote();
        }
        printInput(TWO_HUNDRED, banknoteCount);
    }

    public void putFiveHundred(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            FIVE_HUNDRED.addBanknote();
        }
        printInput(FIVE_HUNDRED, banknoteCount);
    }

    public void putOneThousand(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            ONE_THOUSAND.addBanknote();
        }
        printInput(ONE_THOUSAND, banknoteCount);
    }

    public void putTwoThousand(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            TWO_THOUSAND.addBanknote();
        }
        printInput(TWO_THOUSAND, banknoteCount);
    }

    public void putFiveThousand(int banknoteCount) {
        for (int i = 0; i < banknoteCount; i++) {
            FIVE_THOUSAND.addBanknote();
        }
        printInput(FIVE_THOUSAND, banknoteCount);
    }

    public void extradite(int inputSum) {
        if (inputSum > getTotalCash()) {
            throw new NotEnoughMoney("Not enough money. Try again later");
        }

        int tmpSum = inputSum;

        List<Banknotes> availableBanknotes = Stream.of(
                        FIFTY,
                        ONE_HUNDRED,
                        TWO_HUNDRED,
                        FIVE_HUNDRED,
                        ONE_THOUSAND,
                        TWO_THOUSAND,
                        FIVE_THOUSAND
                )
                .filter(banknote -> banknote.getBanknoteCounter() > 0)
                .sorted(Comparator.comparingInt(Banknotes::getBanknoteValue))
                .collect(Collectors.toList());

        int minimalBanknoteValue = availableBanknotes.stream()
                .min(Comparator.comparingInt(Banknotes::getBanknoteValue))
                .orElseThrow(() -> new NotEnoughMoney("There is no any banknote into ATM"))
                .getBanknoteValue();

        if (inputSum % minimalBanknoteValue != 0) {
            StringBuilder availableBanknotesAsString = new StringBuilder();
            for (Banknotes banknote : availableBanknotes) {
                availableBanknotesAsString.append(banknote.getBanknoteValue()).append(",");
            }
            throw new NotValidSum(String.format("Can't extradite %d. Available banknotes is %s. Please enter sum multiple of %d", inputSum, availableBanknotesAsString, minimalBanknoteValue));
        }

        Collections.reverse(availableBanknotes);

        Map<Banknotes, Integer> toExtradite = new HashMap<>();
        do {
            for (Banknotes banknote : availableBanknotes) {
                var neededBanknotes = inputSum / banknote.getBanknoteValue();
                var banknotesToExtradite = Math.min(neededBanknotes, banknote.getBanknoteCounter());
                if (banknotesToExtradite > 0) {
                    if (tmpSum == banknotesToExtradite * banknote.getBanknoteValue()) {
                        tmpSum = tmpSum - (banknotesToExtradite * banknote.getBanknoteValue());
                        toExtradite.put(banknote, banknotesToExtradite);
                        break;
                    } else if (tmpSum > banknotesToExtradite * banknote.getBanknoteValue()) {
                        tmpSum = tmpSum - (banknotesToExtradite * banknote.getBanknoteValue());
                        toExtradite.put(banknote, banknotesToExtradite);
                    } else {
                        while (tmpSum < banknotesToExtradite * banknote.getBanknoteValue()) {
                            banknotesToExtradite--;
                            if (banknotesToExtradite < 0) {
                                break;
                            }
                        }
                        tmpSum = tmpSum - (banknotesToExtradite * banknote.getBanknoteValue());
                        toExtradite.put(banknote, banknotesToExtradite);
                    }
                }
            }
        } while (tmpSum > minimalBanknoteValue);

        if (tmpSum == 0) {
            toExtradite.forEach((key, value) -> {
                for (int i = 0; i < value; i++) {
                    key.payBanknote();
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

    private void printInput(Banknotes banknote, int banknoteCount) {
        System.out.printf("Banknote %s put %d %s successful\n", banknote.getBanknoteValue(), banknoteCount, banknoteCount > 1 ? "times" : "time");
    }

}
