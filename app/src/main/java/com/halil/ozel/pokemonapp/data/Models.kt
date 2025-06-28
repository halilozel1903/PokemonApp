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
    val sprites: Sprites
)

@Serializable
data class Sprites(
    @SerialName("front_default") val frontDefault: String? = null
)
