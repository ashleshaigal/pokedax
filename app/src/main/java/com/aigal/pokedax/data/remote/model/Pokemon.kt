package com.aigal.pokedax.data.remote.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: Name,
    val type: List<String>,
    val base: Base?, // Mark as nullable to handle missing data
    val species: String?, // Mark as nullable
    val description: String?, // Mark as nullable
    val evolution: Evolution?,
    val profile: Profile?,
    val image: Image? // Mark as nullable to be safe
)

data class Name(
    val english: String
)

data class Base(
    @SerializedName("HP") val hp: Int?, // Mark as nullable
    @SerializedName("Attack") val attack: Int?, // Mark as nullable
    @SerializedName("Defense") val defense: Int?, // Mark as nullable
    @SerializedName("Sp. Attack") val spAttack: Int?, // Mark as nullable
    @SerializedName("Sp. Defense") val spDefense: Int?, // Mark as nullable
    @SerializedName("Speed") val speed: Int? // Mark as nullable
)

data class Image(
    val sprite: String,
    val thumbnail: String,
    val hires: String
)

data class Evolution(
    val next: List<List<String>>?
)

data class Profile(
    val height: String?, // Mark as nullable
    val weight: String?, // Mark as nullable
    val egg: List<String>?, // Mark as nullable
    val ability: List<List<String>>?, // Mark as nullable
    val gender: String? // Mark as nullable
)