package wtf.joni.bitcoinapi.model;

public class DownwardTrendResponse extends ApiResponse {

    int longestTrend;

    public int getLongestTrend() {
        return longestTrend;
    }

    public void setLongestTrend(int longestTrend) {
        this.longestTrend = longestTrend;
    }
}
