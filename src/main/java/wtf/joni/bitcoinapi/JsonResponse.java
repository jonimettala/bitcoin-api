package wtf.joni.bitcoinapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonResponse {
    @JsonProperty("prices")
    private List<Float[]> prices;

    @JsonProperty("market_caps")
    private List<Float[]> marketCaps;

    @JsonProperty("total_volumes")
    private List<Float[]> totalVolumes;

    public List<Float[]> getPrices() {
        return prices;
    }

    public void setPrices(List<Float[]> prices) {
        this.prices = prices;
    }

    public List<Float[]> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<Float[]> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<Float[]> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<Float[]> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }
}
