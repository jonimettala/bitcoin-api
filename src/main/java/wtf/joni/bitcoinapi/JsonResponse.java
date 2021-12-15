package wtf.joni.bitcoinapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonResponse {
    @JsonProperty("prices")
    private List<long[]> prices;

    @JsonProperty("market_caps")
    private List<long[]> marketCaps;

    @JsonProperty("total_volumes")
    private List<long[]> totalVolumes;

    public List<long[]> getPrices() {
        return prices;
    }

    public void setPrices(List<long[]> prices) {
        this.prices = prices;
    }

    public List<long[]> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<long[]> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<long[]> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<long[]> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }
}
