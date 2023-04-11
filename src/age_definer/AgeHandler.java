package age_definer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
    /**
     * This variable is needed for checking of correct input
     */
    final static int DATE_LENGTH = 10;
    String name;
    String surname;
    String patronymic;
    LocalDate birthDate;

    String gender;

    public AgeHandler() {
        while (true) {
            try {
                System.out.println("Введите: Имя Фамилия Отчество Дата рождения");
                getUserInformation();
                break;
            }
            catch (ParseException e) {
                System.out.println("Non-existent date. Try again according to: " + DATE_FORMAT + " format.");
            }
            catch (TimeTravelException e) {
                System.out.println("The person from the future has not yet born. Try again by following rules of time.");
            }
            catch (NonRussianCharException e) {
                System.out.println("Only russian symbols are allowed. Try again.");
            }
            catch (WrongDateException e) {
                System.out.println("Wrong date format. Try again.");
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
     * @throws WrongDateException
     */
    private static LocalDate parseDate(Scanner in) throws TimeTravelException, ParseException, WrongDateException {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        while(true) {
            String date_s = in.next();
            checkDateCorrectness(date_s);

            df.setLenient(false);
            Calendar date_c = Calendar.getInstance();
            date_c.setTime(df.parse(date_s));

            LocalDate date = LocalDate.of(date_c.get(Calendar.YEAR), date_c.get(Calendar.MONTH) + 1, date_c.get(Calendar.DAY_OF_MONTH));
            LocalDate currentDate = LocalDate.now( ZoneId.of("Europe/Moscow"));

            if (currentDate.isBefore(date)) {
                throw new TimeTravelException();
            }
            return date;
        }
    }

    /**
     * @param date to check its correctness
     * @throws WrongDateException
     */
    private static void checkDateCorrectness(String date) throws WrongDateException {
        if (date.length() != DATE_LENGTH) {
            throw new WrongDateException();
        }

        for (int i = 0; i < date.length(); i++) {
            char c = date.charAt(i);

            boolean isCorrectDateSymbol = (c >= '0' && c <= '9');
            if (!isCorrectDateSymbol && (i != 2 && i != 5)) {
                throw new WrongDateException();
            }
        }
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
     * @throws WrongDateException
     * @throws UndefinedGenderException
     */
    private void getUserInformation() throws TimeTravelException,
            ParseException, NonRussianCharException,
            WrongDateException, UndefinedGenderException {
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
        genderDefiner += patronymic.charAt(patronymic.length() - 3);
        genderDefiner += patronymic.charAt(patronymic.length() - 2);
        genderDefiner += patronymic.charAt(patronymic.length() - 1);

        if (genderDefiner.equals("вич")) {
            return "М";
        }
        else if (genderDefiner.equals("вна")) {
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
