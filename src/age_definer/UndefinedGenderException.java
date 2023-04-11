package age_definer;

/**
 * Is being invoked when gender can not be defined
 */
public class UndefinedGenderException extends Exception {
    public UndefinedGenderException(String message) {
        super(message);
    }
    public UndefinedGenderException() {}
}
