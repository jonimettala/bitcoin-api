package wtf.joni.bitcoinapi.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.CountBestBuyAndSellDates;
import wtf.joni.bitcoinapi.processor.CountDownwardTrend;
import wtf.joni.bitcoinapi.processor.CountHighestTradingVolume;
import wtf.joni.bitcoinapi.processor.DecompressBrotli;
import wtf.joni.bitcoinapi.processor.PrepareDateValues;
import wtf.joni.bitcoinapi.processor.PrepareErrorResponse;

@Component
public class BitcoinRoute extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .removeHeaders("*")
                .process(new PrepareErrorResponse())
                .log(LoggingLevel.WARN, "Error: ${body.getDescription}")
                .handled(true)
                .marshal().json(JsonLibrary.Jackson);

        from("direct:downwardTrend")
                .routeId("downwardTrendRoute")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .to("direct:preprocess")
                .log("Trying to count the downward trend")
                .process(new CountDownwardTrend())
                .log("Counting succeed: ${body.getDescription}")
                .to("direct:postprocess");

        from("direct:highestVolume")
                .routeId("highestVolumeRoute")
                .log("New request; from: ${header.from}; to: ${header.to}")
                // 1d offset. Market volume is for the previous 24h, using the following midnight data for each day.
                .setProperty("offset", constant(86400))
                .to("direct:preprocess")
                .log("Trying to count the highest trading volume")
                .process(new CountHighestTradingVolume())
                .log("Counting succeed: ${body.getDescription}")
                .to("direct:postprocess");

        from("direct:timeMachine")
                .routeId("timeMachineRoute")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .to("direct:preprocess")
                .log("Trying to count best buy and sell dates")
                .process(new CountBestBuyAndSellDates())
                .log("Counting succeed: ${body.getDescription}")
                .to("direct:postprocess");

        from("direct:preprocess")
                .routeId("preprocessRoute")
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
                .log("Brotli decompressed");

        from("direct:postprocess")
                .routeId("postprocessRoute")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("application/json; charset=utf-8"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }

    /**
     * Handles LocalDate serialization.
     * @return Default object mapper.
     */
    @Bean
    public ObjectMapper defaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
