import interfaces.Banknotes;
import interfaces.Cell;
import model.ATM;

import java.util.Map;

public class ATMus extends ATM {

    public ATMus(Map<Banknotes, Cell> abstractCellStorage) {
        super(abstractCellStorage);
    }
}
