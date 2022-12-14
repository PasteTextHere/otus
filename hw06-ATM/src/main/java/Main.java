import interfaces.Banknotes;
import interfaces.Cell;
import model.CellDollarImpl;
import model.CellRoubleImpl;
import model.Dollars;
import model.Roubles;

import java.util.Map;

import static model.Roubles.*;

public class Main {
    public static void main(String[] args) {
        Map<Banknotes, Cell> f = Map.of(
                FIFTY, new CellRoubleImpl(FIFTY),
                ONE_HUNDRED, new CellRoubleImpl(ONE_HUNDRED),
                TWO_HUNDRED, new CellRoubleImpl(TWO_HUNDRED),
                FIVE_HUNDRED, new CellRoubleImpl(FIVE_HUNDRED),
                ONE_THOUSAND, new CellRoubleImpl(ONE_THOUSAND),
                TWO_THOUSAND, new CellRoubleImpl(TWO_THOUSAND),
                FIVE_THOUSAND, new CellRoubleImpl(FIVE_THOUSAND));

        var rouble = new ATMRoubles(f);

        var mapInputRoubles = Map.of(Roubles.FIFTY, 5);
        var mapInputDollars = Map.of(Dollars.DOLLAR, 4);

        rouble.showBalance();
        rouble.putBanknotes(mapInputRoubles);
        rouble.showBalance();

        var us = new ATMus(
                Map.of(Dollars.DOLLAR, new CellDollarImpl(Dollars.DOLLAR))
        );
        System.out.println("***************************");
        us.showBalance();
        us.putBanknotes(mapInputDollars);
        us.showBalance();
    }
}
