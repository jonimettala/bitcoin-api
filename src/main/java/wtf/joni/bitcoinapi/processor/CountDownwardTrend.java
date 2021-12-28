package wtf.joni.bitcoinapi.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.joni.bitcoinapi.model.DownwardTrendResponse;

import java.math.BigDecimal;
import java.util.Map;

import static wtf.joni.bitcoinapi.util.CoinGeckoUtils.resolveDailyItems;

public class CountDownwardTrend implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(CountDownwardTrend.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        JsonNode prices = new ObjectMapper().readTree(body).get("prices");

        Map<Long, BigDecimal> dailyValues = resolveDailyItems(prices);

        int longestStreak = 0;
        int currentStreak = 0;
        BigDecimal previousPrice = new BigDecimal("0");

        for (Map.Entry<Long, BigDecimal> entry : dailyValues.entrySet()) {
            if (entry.getValue().compareTo(previousPrice) < 0) {
                currentStreak++;
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
            previousPrice = entry.getValue();

            LOG.debug(entry.getValue() + " " + currentStreak + " " + longestStreak);
        }

        DownwardTrendResponse response = new DownwardTrendResponse();
        response.setDescription("Longest downward trend (days)");
        response.setLongestTrend(longestStreak);

        exchange.getMessage().setBody(response);
    }
}
