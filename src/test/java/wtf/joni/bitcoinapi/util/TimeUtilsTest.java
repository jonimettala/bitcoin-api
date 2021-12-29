package wtf.joni.bitcoinapi.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wtf.joni.bitcoinapi.util.TimeUtils.*;

public class TimeUtilsTest {

    @Test
    public void convertEpochToLocalDate() {
        LocalDate date = convertEpochToDate(1579402947092L);
        assertEquals("2020-01-19", date.toString());
    }

    @Test
    public void convertDateToEpoch() {
        long epoch = 0;
        try {
            epoch = convertToEpoch("2020-01-01");
        } catch (Exception e) {
            System.out.println("Parse error");
        }
        assertEquals(1577836800, epoch);
    }

    @Test
    public void validateDate() {
        assertTrue(dateIsValid("2021-12-24"));
        assertFalse(dateIsValid("2021-1-1"));
        assertFalse(dateIsValid("2021-04-33"));
    }
}


