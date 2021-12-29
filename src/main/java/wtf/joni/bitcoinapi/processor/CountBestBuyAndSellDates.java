package wtf.joni.bitcoinapi.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.joni.bitcoinapi.model.TimeMachineResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static wtf.joni.bitcoinapi.util.CoinGeckoUtils.resolveDailyItems;
import static wtf.joni.bitcoinapi.util.TimeUtils.convertEpochToDate;

public class CountBestBuyAndSellDates implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(CountBestBuyAndSellDates.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getIn().getBody(String.class);
        JsonNode prices = new ObjectMapper().readTree(body).get("prices");

        Map<Long, BigDecimal> dailyValues = resolveDailyItems(prices);

        LocalDate shouldBuy = null;
        LocalDate shouldSell = null;
        BigDecimal highestProfit = new BigDecimal("0");

        for (Map.Entry<Long, BigDecimal> entry : dailyValues.entrySet()) {
            LocalDate possiblyHighestBuyDate = convertEpochToDate(entry.getKey());
            LocalDate possiblyHighestSellDate = null;
            BigDecimal possiblyHighestPrice = entry.getValue();

            for (Map.Entry<Long, BigDecimal> e : dailyValues.entrySet()) {
                LocalDate date = convertEpochToDate(e.getKey());
                if (date.isAfter(possiblyHighestBuyDate) && e.getValue().compareTo(possiblyHighestPrice) > 0) {
                    possiblyHighestPrice = e.getValue();
                    possiblyHighestSellDate = date;
                }
                LOG.debug(entry.getValue() + " " + e.getValue() + " " + possiblyHighestPrice);
            }

            BigDecimal possiblyHighestProfit = possiblyHighestPrice.subtract(entry.getValue());

            if (possiblyHighestProfit.compareTo(highestProfit) > 0) {
                highestProfit = possiblyHighestProfit;
                shouldBuy = possiblyHighestBuyDate;
                shouldSell = possiblyHighestSellDate;
            }

            LOG.debug(entry.getValue() + "; highest: " + highestProfit + "; possiblyHighest: " + possiblyHighestProfit);
        }

        TimeMachineResponse response = new TimeMachineResponse();
        if (shouldBuy != null && shouldSell != null) {
            response.setOptimalBuyDate(shouldBuy);
            response.setOptimalSellDate(shouldSell);
            response.setDescription("Maximise profit by going back to these dates");
        } else {
            response.setDescription("No profit can be made during this time range");
        }

        exchange.getMessage().setBody(response);
    }
}
