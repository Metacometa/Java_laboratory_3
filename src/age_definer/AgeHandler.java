package age_definer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Class that contains age handling methods
 */
public class AgeHandler {
    /**
     * Date format for parsing date from strings
     */
    final static String DATE_FORMAT = "dd.MM.yyyy";
    String name;
    String surname;
    String patronymic;
    LocalDate birthDate;

    String gender;

    public AgeHandler() {
        while (true) {
            try {
                System.out.println("Введите: Фамилия Имя Отчество Дата рождения");
                getUserInformation();
                break;
            }
            catch (ParseException e) {
                System.out.println("Date does not exist. Try again according to: " + DATE_FORMAT + " format.");
            }
            catch (TimeTravelException e) {
                System.out.println("The person from the future has not yet born. Try again by following rules of time.");
            }
            catch (NonRussianCharException e) {
                System.out.println("Only russian symbols are allowed. Try again.");
            }
            catch (UndefinedGenderException e) {
                System.out.println("Sorry. Unsuccessful defining of the gender. Please, try another data");
            }
        }

        printHandledUserInformation();
    }

    /**
     * @param in in order to not create another Scanner
     * @return parsed date
     * @throws TimeTravelException
     * @throws ParseException
     */
    private static LocalDate parseDate(Scanner in) throws TimeTravelException, ParseException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String date_s = in.next();

        checkDateCorrectness(date_s);

        LocalDate date = LocalDate.parse(date_s, dateFormatter);
        LocalDate currentDate = LocalDate.now( ZoneId.of("Europe/Moscow"));

        if (currentDate.isBefore(date)) {
            throw new TimeTravelException();
        }
        return date;
    }

    /**
     * This method is needed to throw exceptions if date has wrong format and also takes into consider leap years
     * @param date input date
     * @throws ParseException
     */
    private static void checkDateCorrectness(String date) throws ParseException {
        DateFormat calendarFormatter = new SimpleDateFormat(DATE_FORMAT);

        calendarFormatter.setLenient(false);
        Calendar date_c = Calendar.getInstance();
        date_c.setTime(calendarFormatter.parse(date));
    }

    /**
     * @param anthroponymy word that should be Russian
     * @throws NonRussianCharException
     */
    private static void checkWordLanguage(String anthroponymy) throws NonRussianCharException {
        for(int i = 0; i < anthroponymy.length(); i++) {
            char c = anthroponymy.charAt(i);
            boolean isRussianSymbol = ((c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я'));
            if (!isRussianSymbol) {
                throw new NonRussianCharException();
            }
        }
    }

    /**
     * In this method the programm gets user surname, name, patronymic and date
     * @throws TimeTravelException
     * @throws ParseException
     * @throws NonRussianCharException
     * @throws UndefinedGenderException
     */
    private void getUserInformation() throws TimeTravelException,
            ParseException, NonRussianCharException, UndefinedGenderException {
        Scanner in = new Scanner(System.in);

        surname = in.next();
        checkWordLanguage(surname);

        name = in.next();
        checkWordLanguage(name);

        patronymic = in.next();
        checkWordLanguage(patronymic);
        gender = getGenderFromPatronymic(patronymic);

        birthDate = parseDate(in);
    }

    /**
     * @param patronymic
     * @return defined gender
     * @throws UndefinedGenderException
     */
    private static String getGenderFromPatronymic(String patronymic) throws UndefinedGenderException{
        String genderDefiner = "";
        genderDefiner += patronymic.charAt(patronymic.length() - 1);

        if (genderDefiner.equals("ч")) {
            return "М";
        }
        else if (genderDefiner.equals("а")) {
            return "Ж";
        }
        else {
            throw new UndefinedGenderException();
        }

    }

    /**
     * Prints "Name Surname[0]. Patronymic[0].   gender   birth date"
     */
    private void printHandledUserInformation() {
        System.out.print(surname + " " + name.charAt(0) + "." + patronymic.charAt(0) + ".   " + gender + "   ");

        printAge(birthDate);

    }

    /**
     * Prints age in a correct form
     * @param birthDate date to calculate age
     */
    private static void printAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
        long years = java.time.temporal.ChronoUnit.YEARS.between(birthDate, currentDate);
        System.out.print(years + " ");
        long lastTwoDigits = years%100;
        long lastDigit = years%10;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 14 || lastDigit == 0 || lastDigit >= 5) {
            System.out.println("лет");
        }
        else if (lastDigit == 1) {
            System.out.println("год");
        }
        else {
            System.out.println("года");
        }
    }
}
