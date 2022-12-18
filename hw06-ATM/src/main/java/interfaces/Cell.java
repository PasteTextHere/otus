package interfaces;

public interface Cell {

    Banknotes cellType();
    void showCellInfo();
    void put(Banknotes banknote);
    void get(Banknotes banknote);
    int getLimit();
    int getPiecesLost();
    boolean isAvailable();
}
