package wtf.joni.bitcoinapi.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.ValidateDatesProcessor;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@Component
public class BitcoinRoute extends RouteBuilder {

    @Override
    public void configure() {

        /*
        onException(Exception.class)
                .process(new ErrorResponseProcessor())
                .log("Error: ${body}")
                .handled(true)
        ;

         */

        from("direct:downward")
                .routeId("downwardRoute")
                .removeHeaders("CamelHttp*")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .process(new ValidateDatesProcessor())
                .setHeader(CONTENT_TYPE, constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .log("Trying to fetch data...")
                .to("{{coingecko.url.base}}&from=1577836800&to=1609376400")
                .log("Data fetched")
                .setHeader(HTTP_RESPONSE_CODE, constant(200))
                //.log("${body}")
        ;
    }
}
