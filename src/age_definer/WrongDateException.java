package age_definer;

/**
 * Is being invoked when date format is wrong
 */
public class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super(message);
    }
    public WrongDateException() {}
}