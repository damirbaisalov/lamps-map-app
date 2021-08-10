package kz.bfgroup.formmap.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("name")
    val name: String?
)