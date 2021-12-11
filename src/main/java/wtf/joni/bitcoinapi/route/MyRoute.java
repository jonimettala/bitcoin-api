package wtf.joni.bitcoinapi.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:myTimer?repeatCount=1")
                .routeId("myTimedRoute")
                .log("oh dog");

        from("direct:test")
                .routeId("testRoute")
                .log("triggered");
    }
}
