package entity;

import java.util.Objects;

/**
 * @author Shabunina Anita
 */
public class NoteCell {

    private final Denomination denomination;
    private final int quantity;

    public NoteCell(Denomination denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteCell noteCell = (NoteCell) o;
        return quantity == noteCell.quantity && denomination == noteCell.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination, quantity);
    }
}
