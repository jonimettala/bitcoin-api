package wtf.joni.bitcoinapi.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import wtf.joni.bitcoinapi.model.ApiResponse;

import java.math.BigDecimal;
import java.util.Map;

import static wtf.joni.bitcoinapi.util.CoinGeckoUtils.resolveDailyItems;

public class CountDownwardTrend implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(body);
        JsonNode prices = json.get("prices");

        Map<Long, BigDecimal> dailyValues = resolveDailyItems(prices);

        int bestStreak = 0;
        int currentStreak = 0;
        BigDecimal previousPrice = new BigDecimal("0");
        for (Map.Entry<Long, BigDecimal> entry : dailyValues.entrySet()) {
            if (entry.getValue().compareTo(previousPrice) < 0) {
                currentStreak++;
                if (currentStreak > bestStreak) {
                    bestStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
            previousPrice = entry.getValue();

            System.out.println(entry.getValue() + " " + currentStreak + " " + bestStreak);
        }

        ApiResponse response = new ApiResponse();

        response.setCode("200");
        response.setMessage("Best downward streak: " + bestStreak);

        exchange.getMessage().setBody(response);

    }
}
