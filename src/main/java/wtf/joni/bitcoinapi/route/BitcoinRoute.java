package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.jetty.http.HttpMethod;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.DateValueProcessor;
import wtf.joni.bitcoinapi.processor.DownwardTrendProcessor;

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
                .streamCaching()
                //.convertBodyTo(String.class, "UTF-8")
                .removeHeaders("CamelHttp*")
                .log("New request; from: ${header.from}; to: ${header.to}")
                .process(new DateValueProcessor())
                .log("Trying to fetch data; from: ${exchangeProperty.fromEpoch}; to: ${exchangeProperty.toEpoch}")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json; charset=utf-8"))
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD).constant(HttpMethod.GET)
                .setHeader(Exchange.CONTENT_ENCODING, constant("deflate"))
                .setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("UTF-8"))
                .setHeader(Exchange.SKIP_GZIP_ENCODING, constant(false))
                .setHeader(Exchange.HTTP_URI, simple("{{coingecko.url.base}}"
                        + "&from=${exchangeProperty.fromEpoch}&to=${exchangeProperty.toEpoch}"))
                .to("http:value.in.headers")
                //.unmarshal().zipDeflater()
                //.convertBodyTo(String.class, "UTF-8")
                //.log("${body}")
                //.convertBodyTo(String.class, "UTF-8")
                //.unmarshal().json(JsonLibrary.Jackson)
                .log("Data fetched, trying to process...")
                .process(new DownwardTrendProcessor())
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                //.log("${body}")
        ;
    }
}
