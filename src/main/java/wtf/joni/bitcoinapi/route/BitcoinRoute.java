package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.DateValueProcessor;

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
                .process(new DateValueProcessor())
                .log("Trying to fetch data; from: ${exchangeProperty.fromEpoch}; to: ${exchangeProperty.toEpoch}")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_URI, simple("{{coingecko.url.base}}"
                        + "&from=${exchangeProperty.fromEpoch}&to=${exchangeProperty.toEpoch}"))
                .to("http:value.in.headers")
                .log("Data fetched")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                //.log("${body}")
        ;
    }
}
