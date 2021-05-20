package entity;

/**
 * @author Shabunina Anita
 */
public enum Denomination {
    FIFTY(50),
    HUNDRED(100),
    FIFE_HUNDRED(500),
    TWO_HUNDRED(200),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private final int val;

    Denomination(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
