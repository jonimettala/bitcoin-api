package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.CountDownwardTrend;
import wtf.joni.bitcoinapi.processor.DateValueProcessor;
import wtf.joni.bitcoinapi.processor.DecompressBrotli;

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
                //.streamCaching()
                .removeHeaders("CamelHttp*")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .process(new DateValueProcessor())
                .log("Trying to fetch data; from: ${exchangeProperty.fromEpoch}; to: ${exchangeProperty.toEpoch}")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_URI, simple("{{coingecko.url.base}}"
                        + "&from=${exchangeProperty.fromEpoch}&to=${exchangeProperty.toEpoch}"))
                .to("http:value.in.headers")
                .log("Data fetched, trying to process...")
                .process(new DecompressBrotli())
                .log("Brotli decompressed, trying to count downward trend...")
                .process(new CountDownwardTrend())
                .log("${body}")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("application/json"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("debug line");
                    }
                })
                //.log("${body}")
        ;
    }
}
