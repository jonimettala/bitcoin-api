package wtf.joni.bitcoinapi.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.model.ApiResponse;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@Component
public class PrepareErrorResponse implements Processor {

    @Override
    public void process(Exchange exchange) {
        String code = exchange.getProperty("errorCode", String.class);
        String message = exchange.getProperty("errorMessage", String.class);

        ApiResponse response = new ApiResponse();
        response.setDescription(message == null ? "Something went wrong when processing the request" : message);

        exchange.getMessage().setHeader(CONTENT_TYPE, "text/plain");
        exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, code == null ? 500 : code);
        exchange.getMessage().setBody(response);
    }
}
