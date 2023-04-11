package age_definer;

/**
 * Is being invoked when word contains not only russian words
 */
public class NonRussianCharException extends Exception {
    public NonRussianCharException(String message) {
        super(message);
    }
    public NonRussianCharException() {}
}

