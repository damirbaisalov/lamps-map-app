package kz.bfgroup.formmap.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GatewayApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("position_x")
    val positionX: String?,
    @JsonProperty("position_y")
    val positionY: String?,
    @JsonProperty("street")
    val street: String?,
    @JsonProperty("district")
    val district: String?,
    @JsonProperty("gateway_id")
    val gatewayId: String?

)