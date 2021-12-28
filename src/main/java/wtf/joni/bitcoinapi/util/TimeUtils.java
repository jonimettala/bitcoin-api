package wtf.joni.bitcoinapi.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TimeUtils {

    /**
     * Convert epoch to its UTC date.
     * @param epoch Epoch
     * @return Day of the epoch
     */
    public static LocalDate convertEpochToDate(long epoch) {
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC")).toLocalDate();
    }
}
