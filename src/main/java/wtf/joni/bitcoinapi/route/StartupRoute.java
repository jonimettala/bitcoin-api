package wtf.joni.bitcoinapi.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StartupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:startupRoute?repeatCount=1")
                .routeId("startupRoute")
                .log("Service running")
        ;
    }
}
