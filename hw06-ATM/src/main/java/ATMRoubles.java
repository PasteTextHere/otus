import interfaces.Banknotes;
import interfaces.Cell;
import model.ATM;

import java.util.Map;

public class ATMRoubles extends ATM {

    public ATMRoubles(Map<Banknotes, Cell> cellStorage) {
        super(cellStorage);
    }
}
