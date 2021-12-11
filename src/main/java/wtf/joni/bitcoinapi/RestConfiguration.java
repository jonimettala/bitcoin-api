package wtf.joni.bitcoinapi;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class RestConfiguration extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(constant("Bad Request"))
                .handled(true)
                ;

        restConfiguration()
                .component("jetty")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/bitcoin/api/v1").port(8080)
                .apiContextPath("api-doc")
                .apiProperty("api.title"," Bitcoin API")
                .apiProperty("api.version", "1.0.0")
                .apiProperty("api.description", "API for checking certain bitcoin information.")
        ;

        rest()
                .get("/downward")
                    .description("Gets how many days is the longest downward rend within a given date range.")
                    .param()
                        .name("from")
                        .dataType("string")
                        .type(RestParamType.query)
                        .required(true)
                        .description("From date for the time range")
                        .example("2021-12-01")
                        .endParam()
                    .param()
                        .name("to")
                        .dataType("string")
                        .type(RestParamType.query)
                        .required(true)
                        .description("To date for the time range")
                        .example("2021-12-24")
                        .endParam()
                    .to("direct:downward")
        ;

    }
}
