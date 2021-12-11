package wtf.joni.bitcoinapi.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@Component
public class ErrorResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String code = exchange.getProperty("errorCode", String.class);
        String message = exchange.getProperty("errorMessage", String.class);

        exchange.getMessage().setHeader(CONTENT_TYPE, "text/plain");
        exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, code == null ? 500 : code);
        exchange.getMessage().setBody(message == null
                ? "Something went wrong when processing the request" : message);
    }
}
