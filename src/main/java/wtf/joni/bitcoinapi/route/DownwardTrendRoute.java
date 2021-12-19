package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.CountDownwardTrend;
import wtf.joni.bitcoinapi.processor.DateValueProcessor;
import wtf.joni.bitcoinapi.processor.DecompressBrotli;
import wtf.joni.bitcoinapi.processor.ErrorResponseProcessor;

@Component
public class DownwardTrendRoute extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .removeHeaders("*")
                .process(new ErrorResponseProcessor())
                .log(LoggingLevel.WARN, "Error: ${body.getDescription}")
                .handled(true)
                .marshal().json(JsonLibrary.Jackson)
        ;

        from("direct:downwardTrend")
                .routeId("downwardTrendRoute")
                .removeHeaders("CamelHttp*")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .process(new DateValueProcessor())
                .log("Trying to fetch data; from: ${exchangeProperty.fromEpoch}; "
                        + "to: ${exchangeProperty.toEpoch}")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_URI, simple("{{coingecko.url.base}}"
                        + "&from=${exchangeProperty.fromEpoch}&to=${exchangeProperty.toEpoch}"))
                .to("http:value.in.headers")
                .log("Data fetched, trying to process...")
                .process(new DecompressBrotli())
                .log("Brotli decompressed, trying to count downward trend...")
                .process(new CountDownwardTrend())
                .log("Downward trend counting succeed, returning API response:\n\n"
                        + "${body.getDescription}: ${body.getValue}")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
