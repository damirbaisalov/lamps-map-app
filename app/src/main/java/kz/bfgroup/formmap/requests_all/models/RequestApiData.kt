package kz.bfgroup.formmap.requests_all.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class RequestApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("user_id")
    val userId: String?,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("create_date")
    val date: String?,
    @JsonProperty("lamp_id")
    val lampId: String?
)