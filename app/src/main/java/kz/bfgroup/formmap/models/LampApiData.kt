package kz.bfgroup.formmap.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LampApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("position_x")
    val positionX: String?,
    @JsonProperty("position_y")
    val positionY: String?,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("brightness")
    val brightness: String?
)