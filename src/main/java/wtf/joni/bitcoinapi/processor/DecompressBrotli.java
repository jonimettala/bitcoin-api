package wtf.joni.bitcoinapi.processor;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.BrotliInputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Component
public class DecompressBrotli implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        byte[] body = exchange.getIn().getBody(byte[].class);

        Brotli4jLoader.ensureAvailability();
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                new BrotliInputStream(
                        new ByteArrayInputStream(body)), StandardCharsets.UTF_8));

        exchange.getMessage().setBody(rd.lines().collect(Collectors.joining()));
    }
}
