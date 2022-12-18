package interfaces;

import java.util.Map;

public interface Input {

    void putBanknotes(Map<? extends Banknotes, Integer> inputBanknotes);
}
