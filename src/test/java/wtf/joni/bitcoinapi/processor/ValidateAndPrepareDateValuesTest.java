package wtf.joni.bitcoinapi.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wtf.joni.bitcoinapi.processor.PrepareDateValues.convertToEpoch;
import static wtf.joni.bitcoinapi.processor.PrepareDateValues.dateIsValid;

public class ValidateAndPrepareDateValuesTest {

    @Test
    public void convertDateToEpoch() {
        int epoch = 0;
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
