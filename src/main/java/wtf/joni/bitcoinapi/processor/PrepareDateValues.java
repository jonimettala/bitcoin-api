package wtf.joni.bitcoinapi.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;

import static wtf.joni.bitcoinapi.util.TimeUtils.*;

@Component
public class PrepareDateValues implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(PrepareDateValues.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String from = exchange.getIn().getHeader("from", String.class);
        String to = exchange.getIn().getHeader("to", String.class);

        if (dateIsValid(from) && dateIsValid(to)) {
            long fromEpoch;
            long toEpoch;

            try {
                fromEpoch = convertToEpoch(from);
                toEpoch = convertToEpoch(to);
            } catch (ParseException e) {
                String message = "Failed to parse date";
                exchange.setProperty("errorMessage", message);
                throw new Exception(message);
            }

            LocalDate fromDate = convertEpochToDate(fromEpoch * 1000L);
            LocalDate toDate = convertEpochToDate(toEpoch * 1000L);
            LOG.debug("from: " + fromDate + "; to: " + toDate);

            if (!toDate.isAfter(fromDate)) {
                String message = "'to' date must be after 'from' date";
                exchange.setProperty("errorMessage", message);
                exchange.setProperty("errorCode", 400);
                throw new IllegalArgumentException(message);
            }

            int offset = 0;
            if (exchange.getProperty("offset") != null) {
                offset = (int) exchange.getProperty("offset");
            }
            LOG.debug("Offset: " + offset);

            exchange.setProperty("fromEpoch", Long.toString(fromEpoch + offset));
            // Adding an hour to get last day's results for sure
            exchange.setProperty("toEpoch", Long.toString(toEpoch + 3600 + offset));
        } else {
            String message = "Invalid 'from' and 'to' headers, both must be in a form of 'yyyy-mm-dd'";
            exchange.setProperty("errorMessage", message);
            exchange.setProperty("errorCode", 400);
            throw new IllegalArgumentException(message);
        }
    }
}
