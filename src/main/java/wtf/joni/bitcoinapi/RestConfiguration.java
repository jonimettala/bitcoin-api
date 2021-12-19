package wtf.joni.bitcoinapi;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;
import wtf.joni.bitcoinapi.processor.ErrorResponseProcessor;

@Component
public class RestConfiguration extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ErrorResponseProcessor())
                .log("Error: ${body}")
                .handled(true)
        ;

        restConfiguration()
                .component("jetty")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/bitcoin/api/v1")
                .apiContextPath("api-doc")
                .apiProperty("api.title", "Bitcoin API")
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
                    .to("direct:downwardTrend")
        ;
    }
}
