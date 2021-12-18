package wtf.joni.bitcoinapi.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class DateValueProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String from = exchange.getIn().getHeader("from", String.class);
        String to = exchange.getIn().getHeader("to", String.class);

        if (dateIsValid(from) && dateIsValid(to)) {
            int fromEpoch;
            int toEpoch;

            try {
                fromEpoch = convertToEpoch(from);
                toEpoch = convertToEpoch(to);
            } catch (ParseException e) {
                String message = "Failed to parse date";
                exchange.setProperty("errorMessage", message);
                throw new Exception(message);
            }

            exchange.setProperty("fromEpoch", Integer.toString(fromEpoch));
            // Adding an hour to get last day's results for sure
            exchange.setProperty("toEpoch", Integer.toString(toEpoch + 3600));
        } else {
            String message = "Invalid 'from' and 'to' headers, both must be in a form of 'yyyy-mm-dd'";
            exchange.setProperty("errorMessage", message);
            exchange.setProperty("errorCode", 400);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates date is in a form of yyyy-mm-dd.
     * @param maybeDate Possibly a date.
     * @return True if the date is in the valid form.
     */
    protected static boolean dateIsValid(String maybeDate) {
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
    static protected int convertToEpoch(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS zzz");
        Date dt = sdf.parse(date + "T00:00:00.000 UTC");
        long epoch = dt.getTime();
        return (int)(epoch/1000);
    }
}
