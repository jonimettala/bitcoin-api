package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.CountDownwardTrend;
import wtf.joni.bitcoinapi.processor.CountHighestTradingVolume;
import wtf.joni.bitcoinapi.processor.DecompressBrotli;
import wtf.joni.bitcoinapi.processor.PrepareErrorResponse;
import wtf.joni.bitcoinapi.processor.PrepareDateValues;

@Component
public class BitcoinRoute extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .removeHeaders("*")
                .process(new PrepareErrorResponse())
                .log(LoggingLevel.WARN, "Error: ${body.getDescription}")
                .handled(true)
                .marshal().json(JsonLibrary.Jackson)
        ;

        from("direct:downwardTrend")
                .routeId("downwardTrendRoute")
                .to("direct:preprocess")
                .log("Trying to count the downward trend")
                .process(new CountDownwardTrend())
                .log("Downward trend counting succeed, returning API response:\n\n"
                        + "${body.getDescription}: ${body.getValue}")
                .to("direct:postprocess")
        ;

        from("direct:highestVolume")
                .routeId("highestVolumeRoute")
                // 1d offset. Market volume is for the previous 24h, using the following midnight data for each day.
                .setProperty("offset", constant(86400))
                .to("direct:preprocess")
                .process(new CountHighestTradingVolume())
                .unmarshal().json()
                .to("direct:postprocess")
        ;

        from("direct:preprocess")
                .routeId("preprocessRoute")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .process(new PrepareDateValues())
                .removeHeaders("*")
                .log("Trying to fetch data; from: ${exchangeProperty.fromEpoch}; "
                        + "to: ${exchangeProperty.toEpoch}")
                .setHeader("Accept", constant("application/json; utf-8"))
                .setHeader("Accept-Encoding", constant("br"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_URI, simple("{{coingecko.url.base}}"
                        + "&from=${exchangeProperty.fromEpoch}&to=${exchangeProperty.toEpoch}"))
                .to("http:value.in.headers")
                .log("Data fetched, trying to decompress...")
                .process(new DecompressBrotli())
                .log("Brotli decompressed")
        ;

        from("direct:postprocess")
                .routeId("postprocessRoute")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
