package com.halil.ozel.pokemonapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

import androidx.compose.runtime.mutableStateListOf

class PokemonRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    val favorites = mutableStateListOf<String>()

    suspend fun fetchPokemonList(): List<PokemonResult> {
        val response: PokemonListResponse =
            client.get("https://pokeapi.co/api/v2/pokemon?limit=151").body()
        return response.results
    }

    suspend fun fetchPokemonDetail(name: String): PokemonDetail {
        return client.get("https://pokeapi.co/api/v2/pokemon/$name").body()
    }

    fun toggleFavorite(name: String) {
        if (favorites.contains(name)) {
            favorites.remove(name)
        } else {
            favorites.add(name)
        }
    }

    fun isFavorite(name: String): Boolean = favorites.contains(name)
}
