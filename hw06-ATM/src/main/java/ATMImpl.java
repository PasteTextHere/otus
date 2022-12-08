import model.BanknoteEmpty;
import model.NotEnoughMoney;
import model.NotValidSum;

public class ATMImpl extends ATM {

    @Override
    public void extradite(int inputSum) {
        try {
            super.extradite(inputSum);
        } catch (BanknoteEmpty | NotEnoughMoney | NotValidSum e) {
            System.out.println("Catch and handle here");
            throw e;
        }
    }
}
