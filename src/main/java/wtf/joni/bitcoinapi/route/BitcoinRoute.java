package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.ErrorResponseProcessor;
import wtf.joni.bitcoinapi.processor.ValidateDatesProcessor;

@Component
public class BitcoinRoute extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ErrorResponseProcessor())
                .log("Error: ${body}")
                .handled(true)
        ;

        from("direct:downward")
                .routeId("downwardRoute")
                .log("from: ${header.from}; to: ${header.to}")
                .process(new ValidateDatesProcessor())
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(constant("oh dog"))
        ;
    }
}
