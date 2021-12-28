package wtf.joni.bitcoinapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HighestVolumeResponse extends ApiResponse {

    LocalDate date;
    BigDecimal volume;
    String currency;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
