package com.halil.ozel.pokemonapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

import androidx.compose.runtime.mutableStateListOf

import com.halil.ozel.pokemonapp.data.ApiConstants
import com.halil.ozel.pokemonapp.data.PokemonSpecies
import com.halil.ozel.pokemonapp.data.EvolutionChain
import com.halil.ozel.pokemonapp.data.ChainLink
import com.halil.ozel.pokemonapp.data.EvolutionPokemon

class PokemonRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    val favorites = mutableStateListOf<String>()

    suspend fun fetchPokemonList(): List<PokemonResult> {
        val response: PokemonListResponse =
            client.get("${ApiConstants.POKEMON_ENDPOINT}?limit=${ApiConstants.DEFAULT_LIMIT}").body()
        return response.results
    }

    suspend fun fetchPokemonDetail(name: String): PokemonDetail {
        return client.get("${ApiConstants.POKEMON_ENDPOINT}/$name").body()
    }

    fun toggleFavorite(name: String) {
        if (favorites.contains(name)) {
            favorites.remove(name)
        } else {
            favorites.add(name)
        }
    }

    fun isFavorite(name: String): Boolean = favorites.contains(name)

    private fun idFromUrl(url: String): Int =
        url.trimEnd('/').split("/").last().toIntOrNull() ?: 0

    suspend fun fetchEvolutionNames(name: String): List<EvolutionPokemon> {
        val species: PokemonSpecies =
            client.get("${ApiConstants.SPECIES_ENDPOINT}/$name").body()
        val chainUrl = species.evolutionChain.url
        val chain: EvolutionChain = client.get(chainUrl).body()
        val result = mutableListOf<EvolutionPokemon>()

        fun traverse(link: ChainLink) {
            result.add(EvolutionPokemon(link.species.name, idFromUrl(link.species.url)))
            link.evolvesTo.forEach { traverse(it) }
        }
        traverse(chain.chain)
        return result
    }
}
