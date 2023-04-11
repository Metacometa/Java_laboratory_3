package age_definer;

/**
 * Is being invoked when input date is not correlated to current date
 */
public class TimeTravelException extends Exception {
    public TimeTravelException(String message) {
        super(message);
    }
    public TimeTravelException() {}
}

