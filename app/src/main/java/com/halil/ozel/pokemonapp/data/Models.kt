package com.halil.ozel.pokemonapp.data

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PokemonListResponse(
    val results: List<PokemonResult>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PokemonResult(
    val name: String,
    val url: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val weight: Int? = null,
    val height: Int? = null,
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<TypeSlot> = emptyList()
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Sprites(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: OtherSprites? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class OtherSprites(
    @SerialName("official-artwork") val officialArtwork: OfficialArtwork? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class OfficialArtwork(
    @SerialName("front_default") val frontDefault: String? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TypeSlot(
    val slot: Int,
    val type: Type
)

@Serializable
data class Type(
    val name: String
)
