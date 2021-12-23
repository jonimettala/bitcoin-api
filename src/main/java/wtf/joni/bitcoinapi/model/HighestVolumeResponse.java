package wtf.joni.bitcoinapi.model;

import java.math.BigDecimal;

public class HighestVolumeResponse extends ApiResponse {

    String date;
    BigDecimal volume;
    String currency;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
