package wtf.joni.bitcoinapi.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoinGeckoUtils {

    /**
     * Resolve a list of daily values. Drops additional values so that one date will have only one value.
     * Leaves the first value after midnight (UTC).
     * @param json JSON to resolve.
     * @return List of daily values.
     */
    public static Map<Long, BigDecimal> resolveDailyItems(JsonNode json) {
        Map<Long, BigDecimal> dailyItems = new LinkedHashMap<>();

        // System.out.println(json);

        LocalDateTime previousDate = null;

        for (JsonNode item : json) {
            //System.out.println(item);

            Long epoch = Long.parseLong(item.get(0).asText());
            //System.out.println(epoch);

            ZonedDateTime zdt = Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC"));
            LocalDateTime dateTime = zdt.toLocalDateTime();

            // Looping though the list, adding the first value of each date
            if (previousDate == null || dateTime.getDayOfMonth() != previousDate.getDayOfMonth()) {
                dailyItems.put(epoch, new BigDecimal(item.get(1).asText()));
            }
            previousDate = dateTime;

            // System.out.println(dateTime);
        }
        //System.out.println(dailyItems);

        return dailyItems;
    }
}
