package org.ilyutsik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoordinatesDto {

    @JsonProperty("lat")
    private BigDecimal latitude;

    @JsonProperty("lon")
    private BigDecimal longitude;

}
