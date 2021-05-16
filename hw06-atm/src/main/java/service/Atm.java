package service;

import entity.Denomination;
import entity.NoteCell;

import java.util.List;
import java.util.Map;

/**
 * @author Shabunina Anita
 */
public interface Atm {

    /**
     * Внести купюры
     *
     * @param noteCells дополнить ячейки с купюрами
     */
    void addNotes(List<NoteCell> noteCells);

    /**
     * Снять купюры
     *
     * @param amount сумма
     * @return номинал-количество купюр
     */
    Map<Denomination, Integer> withdraw(int amount);

    /**
     * Посчитать сумму средств в банкомате
     *
     * @return сумма средств в банкомате
     */
    int countBalance();
}
