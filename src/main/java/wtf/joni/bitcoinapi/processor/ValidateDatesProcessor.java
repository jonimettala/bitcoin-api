package wtf.joni.bitcoinapi.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidateDatesProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws IllegalArgumentException {
        String from =  exchange.getIn().getHeader("from", String.class);
        String to =  exchange.getIn().getHeader("to", String.class);
        String regex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        // https://stackoverflow.com/questions/22061723/regex-date-validation-for-yyyy-mm-dd/22061879#22061879

        if (from == null || to == null || !Pattern.matches(regex, from) || !Pattern.matches(regex, to)) {
            String message = "Invalid 'from' and 'to' headers, must be in a form of 'yyyy-mm-dd'";
            exchange.setProperty("errorMessage", message);
            exchange.setProperty("errorCode", 400);
            throw new IllegalArgumentException(message);
        }
    }
}
