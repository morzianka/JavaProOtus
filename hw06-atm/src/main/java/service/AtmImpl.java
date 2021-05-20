package service;

import entity.Denomination;
import entity.NoteCell;
import exception.AmountExceededException;

import java.util.*;

/**
 * @author Shabunina Anita
 */
public class AtmImpl implements Atm {

    private final TreeMap<Denomination, Integer> denominationQuantity;

    public AtmImpl(Map<Denomination, Integer> notes) {
        this.denominationQuantity = new TreeMap<>((o1, o2) -> o2.getVal() - o1.getVal());
        this.denominationQuantity.putAll(notes);
    }

    @Override
    public void addNotes(List<NoteCell> noteCells) {
        noteCells.forEach(noteCell -> {
            denominationQuantity.computeIfPresent(noteCell.getDenomination(), (k, v) -> v += noteCell.getQuantity());
            denominationQuantity.putIfAbsent(noteCell.getDenomination(), noteCell.getQuantity());
        });
    }

    @Override
    public Map<Denomination, Integer> withdraw(int amount) {
        int result = 0;
        Map<Denomination, Integer> withdrawNotes = new HashMap<>();
        Map<Denomination, Integer> copy = new TreeMap<>(denominationQuantity);
        for (Map.Entry<Denomination, Integer> entry : denominationQuantity.entrySet()) {
            if (entry.getKey().getVal() < amount) {
                while (result + entry.getKey().getVal() <= amount && copy.get(entry.getKey()) > 0) {
                    result += entry.getKey().getVal();
                    withdrawNotes.computeIfPresent(entry.getKey(), (k, v) -> ++v);
                    withdrawNotes.putIfAbsent(entry.getKey(), 1);
                    copy.computeIfPresent(entry.getKey(), (k, v) -> v - 1);
                }
            }
        }
        if (result == amount) {
            denominationQuantity.putAll(copy);
            return withdrawNotes;
        }
        throw new AmountExceededException("Недостаточно средств в банкомате");
    }

    @Override
    public int countBalance() {
        return denominationQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getVal() * entry.getValue())
                .reduce(0, Integer::sum);
    }

    public Map<Denomination, Integer> getDenominationQuantity() {
        return Collections.unmodifiableMap(denominationQuantity);
    }
}
