import model.NotEnoughMoney;
import model.NotValidSum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMImplTest {

    private ATMImpl atmMachine;


    @BeforeEach
    void setUp() {
        atmMachine = new ATMImpl();
        emptyATM();
    }

    @Test
    @DisplayName("Опустошить банкомат")
    public void ejectAll() {
        putEachBanknoteTenTimes();
        emptyATM();
        assertEquals(0, atmMachine.getTotalCash());
    }

    @Test
    @DisplayName("Внести 50, а потом снять")
    public void put50ThenEjectIt() {
        atmMachine.putFifty(1);
        assertEquals(50, atmMachine.getTotalCash());
        atmMachine.extradite(50);
        assertEquals(0, atmMachine.getTotalCash());
    }

    @Test
    @DisplayName("Внести 50, а потом снять 150")
    public void put50ThenEject150() {
        atmMachine.putFifty(1);
        assertEquals(50, atmMachine.getTotalCash());

        Assertions.assertThrows(NotEnoughMoney.class, () -> atmMachine.extradite(150));
    }

    @Test
    @DisplayName("Сума не кратна минимальной банкноте")
    public void dontMultipleTo50() {
        putEachBanknoteTenTimes();
        Assertions.assertThrows(NotValidSum.class, () -> atmMachine.extradite(20));
    }

    private void emptyATM() {
        if (atmMachine.getTotalCash() > 0) {
            atmMachine.extradite(atmMachine.getTotalCash());
        }
    }

    private void putEachBanknoteTenTimes() {
        atmMachine.putFifty(10);
        atmMachine.putOneHundred(10);
        atmMachine.putTwoHundred(10);
        atmMachine.putFiveHundred(10);
        atmMachine.putOneThousand(10);
        atmMachine.putTwoThousand(10);
        atmMachine.putFiveThousand(10);
    }
}