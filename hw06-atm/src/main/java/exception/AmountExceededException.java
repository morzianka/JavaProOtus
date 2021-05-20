package exception;

/**
 * @author Shabunina Anita
 */
public class AmountExceededException extends RuntimeException {

    public AmountExceededException(String message) {
        super(message);
    }
}
