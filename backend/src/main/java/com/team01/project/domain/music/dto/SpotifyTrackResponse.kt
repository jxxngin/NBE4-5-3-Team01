package com.team01.project.domain.music.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SpotifyTrackResponse(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("uri")
    val uri: String,

    @JsonProperty("artists")
    val artists: List<Artist>,

    @JsonProperty("album")
    val album: Album
) {
    fun getArtistsAsString(): String {
        return artists.joinToString(", ") { it.name ?: "이름 없음" }
    }

    fun getArtistsIdAsString(): String {
        return artists.joinToString(", ") { it.id }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Album(
        @JsonProperty("name")
        val name: String,

        @JsonProperty("release_date")
        val releaseDate: String?,

        @JsonProperty("images")
        val images: List<Image>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Artist(
        @JsonProperty("id")
        val id: String,

        @JsonProperty("name")
        val name: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Image(
        @JsonProperty("url")
        val url: String
    )
}
