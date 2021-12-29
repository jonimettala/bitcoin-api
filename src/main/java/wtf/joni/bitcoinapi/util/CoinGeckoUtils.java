package wtf.joni.bitcoinapi.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static wtf.joni.bitcoinapi.util.TimeUtils.convertEpochToDate;

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

        LocalDate previousDate = null;
        for (JsonNode item : json) {
            long epoch = Long.parseLong(item.get(0).asText());
            LocalDate date = convertEpochToDate(epoch);

            // Looping though the list, adding the first value of each date
            if (previousDate == null || date.getDayOfMonth() != previousDate.getDayOfMonth()) {
                dailyItems.put(epoch, new BigDecimal(item.get(1).asText()));
            }
            previousDate = date;
        }
        LOG.debug("DAILY ITEMS (out):\n" + dailyItems);

        return dailyItems;
    }
}
