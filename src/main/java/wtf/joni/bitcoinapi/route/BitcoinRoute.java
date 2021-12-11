package wtf.joni.bitcoinapi.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.ErrorResponseCodeProcessor;
import wtf.joni.bitcoinapi.processor.ValidateDatesProcessor;

@Component
public class BitcoinRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(IllegalArgumentException.class)
                .to("direct:setCommonErrorInfo")
                .handled(true)
        ;

        onException(Exception.class)
                .to("direct:setCommonErrorInfo")
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

        from("direct:setCommonErrorInfo")
                .routeId("setCommonErrorInfoRoute")
                .process(new ErrorResponseCodeProcessor())
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
        ;
    }
}
