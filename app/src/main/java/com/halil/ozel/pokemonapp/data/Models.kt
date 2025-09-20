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

@Serializable
data class PokemonSpecies(
    @SerialName("evolution_chain") val evolutionChain: EvolutionChainLink
)

@Serializable
data class EvolutionChainLink(
    val url: String
)

@Serializable
data class EvolutionChain(
    val chain: ChainLink
)

@Serializable
data class ChainLink(
    val species: NamedApiResource,
    @SerialName("evolves_to") val evolvesTo: List<ChainLink> = emptyList()
)

@Serializable
data class NamedApiResource(
    val name: String,
    val url: String
)

data class EvolutionPokemon(
    val name: String,
    val id: Int
)

// Modern UI state management classes
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()
}

data class PokemonListUiState(
    val pokemonList: List<PokemonResult> = emptyList(),
    val pokemonTypes: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class PokemonDetailUiState(
    val pokemon: PokemonDetail? = null,
    val evolutions: List<EvolutionPokemon> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
