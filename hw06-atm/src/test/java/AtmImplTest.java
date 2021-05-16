import entity.Denomination;
import entity.NoteCell;
import exception.AmountExceededException;
import org.junit.jupiter.api.Test;
import service.AtmImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmImplTest {

    AtmImpl atm = new AtmImpl(emptyMap());

    @Test
    void addNotes() {
        List<NoteCell> noteCells = List.of(
                new NoteCell(Denomination.FIFTY, 17),
                new NoteCell(Denomination.FIFTY, 5),
                new NoteCell(Denomination.THOUSAND, 3));
        atm.addNotes(noteCells);

        Map<Denomination, Integer> expected = new HashMap<>();
        expected.put(Denomination.FIFTY, 22);
        expected.put(Denomination.THOUSAND, 3);

        assertEquals(expected, atm.getDenominationQuantity());
    }

    @Test
    void countBalance() {
        List<NoteCell> noteCells = List.of(
                new NoteCell(Denomination.TWO_HUNDRED, 2),
                new NoteCell(Denomination.TWO_THOUSAND, 9));
        atm.addNotes(noteCells);
        assertEquals(18400, atm.countBalance());
    }

    @Test
    void countBalance_zero() {
        assertEquals(0, atm.countBalance());
    }

    @Test
    void withdraw() {
        List<NoteCell> noteCells = List.of(
                new NoteCell(Denomination.FIFTY, 150),
                new NoteCell(Denomination.HUNDRED, 100),
                new NoteCell(Denomination.THOUSAND, 3));
        atm.addNotes(noteCells);

        Map<Denomination, Integer> expectedWithdraw = new HashMap<>();
        expectedWithdraw.put(Denomination.FIFTY, 1);
        expectedWithdraw.put(Denomination.HUNDRED, 3);
        expectedWithdraw.put(Denomination.THOUSAND, 1);

        Map<Denomination, Integer> expectedLeft = new HashMap<>();
        expectedLeft.put(Denomination.FIFTY, 149);
        expectedLeft.put(Denomination.HUNDRED, 97);
        expectedLeft.put(Denomination.THOUSAND, 2);

        assertEquals(expectedWithdraw, atm.withdraw(1350));
        assertEquals(expectedLeft, atm.getDenominationQuantity());
    }

    @Test
    void withdrawAmountExceed() {
        List<NoteCell> noteCells = List.of(
                new NoteCell(Denomination.FIFTY, 3),
                new NoteCell(Denomination.HUNDRED, 5));
        atm.addNotes(noteCells);

        Map<Denomination, Integer> expected = new TreeMap<>(atm.getDenominationQuantity());
        assertThrows(AmountExceededException.class, () -> atm.withdraw(5000));
        assertEquals(expected, atm.getDenominationQuantity());
    }
}
