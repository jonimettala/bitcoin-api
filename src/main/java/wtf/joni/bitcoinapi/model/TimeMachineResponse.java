package wtf.joni.bitcoinapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


public class TimeMachineResponse extends ApiResponse {

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate optimalBuyDate;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate optimalSellDate;

    public LocalDate getOptimalBuyDate() {
        return optimalBuyDate;
    }

    public void setOptimalBuyDate(LocalDate optimalBuyDate) {
        this.optimalBuyDate = optimalBuyDate;
    }

    public LocalDate getOptimalSellDate() {
        return optimalSellDate;
    }

    public void setOptimalSellDate(LocalDate optimalSellDate) {
        this.optimalSellDate = optimalSellDate;
    }
}
