package kz.bfgroup.formmap.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("token")
    val token: String?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("phone")
    val phone: String?,
    @JsonProperty("role")
    val role: String?
)