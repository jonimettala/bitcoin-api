package wtf.joni.bitcoinapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
