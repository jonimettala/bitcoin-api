package wtf.joni.bitcoinapi.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.joni.bitcoinapi.model.HighestVolumeResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static wtf.joni.bitcoinapi.util.CoinGeckoUtils.resolveDailyItems;

public class CountHighestTradingVolume implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(CountHighestTradingVolume.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getIn().getBody(String.class);
        JsonNode volumes = new ObjectMapper().readTree(body).get("total_volumes");

        Map<Long, BigDecimal> dailyVolumes = resolveDailyItems(volumes);

        BigDecimal highestVolume = new BigDecimal("0");
        Long highestDate = 0L;

        for (Map.Entry<Long, BigDecimal> volume : dailyVolumes.entrySet()) {
            if (volume.getValue().compareTo(highestVolume) > 0) {
                highestVolume = volume.getValue();
                highestDate = volume.getKey();
            }
            LOG.debug(volume + "; highest: " + highestVolume);
        }
        LOG.debug("Highest: " + highestDate + " : " + highestVolume);

        /*
        Volume is for the previous 24h. We have the epoch for just after midnight, meaning that the total volume
        really concerned the previous day. Therefore, minus one day in LocalDate.
         */
        ZonedDateTime zdt = Instant.ofEpochMilli(highestDate).atZone(ZoneId.of("UTC"));
        LocalDate dateTime = zdt.toLocalDate().minusDays(1);

        HighestVolumeResponse response = new HighestVolumeResponse();
        response.setDescription("Highest volume");
        response.setDate(dateTime);
        response.setVolume(highestVolume);
        response.setCurrency("EUR");

        exchange.getMessage().setBody(response);
    }
}
