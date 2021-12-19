package wtf.joni.bitcoinapi.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoinGeckoUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CoinGeckoUtils.class);

    /**
     * Resolve a list of daily values. Drops additional values so that one date will have only one value.
     * Leaves the first value after midnight (UTC).
     * @param json JSON to resolve.
     * @return List of daily values.
     */
    public static Map<Long, BigDecimal> resolveDailyItems(JsonNode json) {
        LOG.debug("DAILY ITEMS (in): \n" + json.toString());

        Map<Long, BigDecimal> dailyItems = new LinkedHashMap<>();

        LocalDateTime previousDate = null;
        for (JsonNode item : json) {
            Long epoch = Long.parseLong(item.get(0).asText());

            ZonedDateTime zdt = Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC"));
            LocalDateTime dateTime = zdt.toLocalDateTime();

            // Looping though the list, adding the first value of each date
            if (previousDate == null || dateTime.getDayOfMonth() != previousDate.getDayOfMonth()) {
                dailyItems.put(epoch, new BigDecimal(item.get(1).asText()));
            }
            previousDate = dateTime;
        }
        LOG.debug("DAILY ITEMS (out):\n" + dailyItems);

        return dailyItems;
    }
}
