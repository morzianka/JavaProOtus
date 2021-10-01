package ru.otus;

/**
 * @author Shabunina Anita
 */
public class Counter {
    private boolean desc;
    private int count = 1;
    private boolean needToChange;

    public int getCount() {
        if (needToChange) {
            changeCount();
        }
        return count;
    }

    public void switchNeedToChange() {
        needToChange = !needToChange;
    }

    public void changeCount() {
        checkOrder();
        count = desc ? count - 1 : count + 1;
    }

    private void checkOrder() {
        if (count == 10) {
            desc = true;
        } else if (count == 1) {
            desc = false;
        }
    }
}
