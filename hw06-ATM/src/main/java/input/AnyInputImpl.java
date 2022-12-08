package input;

import model.Banknotes;

public interface AnyInputImpl
        extends InputFifty, InputHundred, InputTwoHundred, InputFiveHundred,
        InputOneThousand, InputTwoThousand, InputFiveThousand {

    @Override
    default void inputBanknoteFifty() {
        Banknotes.FIFTY.addBanknote();
    }

    @Override
    default void inputBanknoteFiveHundred() {
        Banknotes.ONE_HUNDRED.addBanknote();
    }

    @Override
    default void inputBanknoteFiveThousand() {
        Banknotes.TWO_HUNDRED.addBanknote();
    }

    @Override
    default void inputBanknoteOneHundred() {
        Banknotes.FIVE_HUNDRED.addBanknote();
    }

    @Override
    default void inputBanknoteOneThousand() {
        Banknotes.ONE_THOUSAND.addBanknote();
    }

    @Override
    default void inputBanknoteTwoHundred() {
        Banknotes.TWO_THOUSAND.addBanknote();
    }

    @Override
    default void inputBanknoteTwoThousand() {
        Banknotes.FIVE_THOUSAND.addBanknote();
    }
}
