package com.halil.ozel.pokemonapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class PokemonRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) { 
            json(Json { 
                ignoreUnknownKeys = true
                coerceInputValues = true
            }) 
        }
    }

    val favorites = mutableStateListOf<String>()

    suspend fun fetchPokemonList(): Result<List<PokemonResult>> = withContext(Dispatchers.IO) {
        try {
            val response: PokemonListResponse =
                client.get("${ApiConstants.POKEMON_ENDPOINT}?limit=${ApiConstants.DEFAULT_LIMIT}").body()
            Result.success(response.results)
        } catch (e: Exception) {
            Log.e("PokemonRepository", "Error fetching Pokemon list", e)
            Result.failure(e)
        }
    }

    suspend fun fetchPokemonDetail(name: String): Result<PokemonDetail> = withContext(Dispatchers.IO) {
        try {
            val detail: PokemonDetail = client.get("${ApiConstants.POKEMON_ENDPOINT}/$name").body()
            Result.success(detail)
        } catch (e: Exception) {
            Log.e("PokemonRepository", "Error fetching Pokemon detail for $name", e)
            Result.failure(e)
        }
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

    suspend fun fetchEvolutionNames(name: String): Result<List<EvolutionPokemon>> = withContext(Dispatchers.IO) {
        try {
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
            Result.success(result)
        } catch (e: Exception) {
            Log.e("PokemonRepository", "Error fetching evolution data for $name", e)
            Result.failure(e)
        }
    }

    fun cleanup() {
        client.close()
    }
}
