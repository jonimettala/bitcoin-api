package wtf.joni.bitcoinapi.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import wtf.joni.bitcoinapi.JsonResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CountDownwardTrend implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String response = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        JsonNode prices = json.get("prices");

        int bestStreak = 0;
        int currentStreak = 0;
        BigDecimal previousPrice = new BigDecimal("0");
        for (JsonNode day : prices) {
            System.out.println(day.get(1).asLong());
            String str = day.get(1).asText();
            System.out.println(str);
            BigDecimal p = new BigDecimal(str);
            System.out.println(p);

            //long price = day.get(1).asLong();
            //if (p > previousPrice) {
            if (p.compareTo(previousPrice) < 0) {
                currentStreak++;
                if (currentStreak > bestStreak) {
                    bestStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
            previousPrice = new BigDecimal(p.toString());

            System.out.println(p + " " + currentStreak + " " + bestStreak);

            JsonResponse r = exchange.getIn().getBody(JsonResponse.class);
            if (r != null) {
                System.out.println("eioo nullia!");
            }

        }

        //System.out.println(prices);



    }
}
