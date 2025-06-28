package com.halil.ozel.pokemonapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val results: List<PokemonResult>
)

@Serializable
data class PokemonResult(
    val name: String,
    val url: String
)

@Serializable
data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val weight: Int? = null,
    val height: Int? = null,
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<TypeSlot> = emptyList(),
    val stats: List<StatSlot> = emptyList()
)

@Serializable
data class Sprites(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: OtherSprites? = null
)

@Serializable
data class OtherSprites(
    @SerialName("official-artwork") val officialArtwork: OfficialArtwork? = null
)

@Serializable
data class OfficialArtwork(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class TypeSlot(
    val slot: Int,
    val type: Type
)

@Serializable
data class Type(
    val name: String
)

@Serializable
data class StatSlot(
    @SerialName("base_stat") val baseStat: Int,
    val stat: StatDetail
)

@Serializable
data class StatDetail(
    val name: String
)
