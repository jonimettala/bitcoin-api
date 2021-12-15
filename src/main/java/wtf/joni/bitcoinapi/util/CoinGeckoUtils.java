package wtf.joni.bitcoinapi.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CoinGeckoUtils {

    protected static Map<Long, BigDecimal> resolveDailyItems(JsonNode json) {
        Map<Long, BigDecimal> dailyItems = new HashMap<>();

        System.out.println(json);

        LocalDateTime previousDate = null;

        for (JsonNode item : json) {
            System.out.println(item);

            Long epoch = Long.parseLong(item.get(0).asText());
            System.out.println(epoch);

            ZonedDateTime zdt = Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC"));

            LocalDateTime dateTime = zdt.toLocalDateTime();

            if (previousDate == null || dateTime.getDayOfMonth() != previousDate.getDayOfMonth()) {
                dailyItems.put(epoch, new BigDecimal(item.get(1).asText()));
            }
            previousDate = dateTime;

            System.out.println(dateTime);

        }

        System.out.println(dailyItems);

        return dailyItems;
    }
}
