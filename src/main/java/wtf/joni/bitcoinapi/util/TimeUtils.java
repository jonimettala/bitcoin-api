package wtf.joni.bitcoinapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class TimeUtils {

    /**
     * Convert epoch to its UTC date.
     * @param epoch Epoch
     * @return Day of the epoch
     */
    public static LocalDate convertEpochToDate(long epoch) {
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC")).toLocalDate();
    }

    /**
     * Validates date is in a form of yyyy-mm-dd.
     * @param maybeDate Possibly a date.
     * @return True if the date is in the valid form.
     */
    public static boolean dateIsValid(String maybeDate) {
        String regex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        // https://stackoverflow.com/questions/22061723/regex-date-validation-for-yyyy-mm-dd/22061879#22061879
        return maybeDate != null && Pattern.matches(regex, maybeDate);
    }

    /**
     * Converts date to UTC Epoch time.
     * @param date Date to convert.
     * @return Date in UTC Epoch time
     * @throws ParseException Error when parsing the date.
     */
    public static long convertToEpoch(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS zzz");
        Date dt = sdf.parse(date + "T00:00:00.000 UTC");
        long epoch = dt.getTime();
        return (long)(epoch / 1000);
    }
}
