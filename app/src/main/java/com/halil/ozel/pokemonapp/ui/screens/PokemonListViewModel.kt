package com.halil.ozel.pokemonapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.PokemonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonResult>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResult>> = _pokemonList.asStateFlow()

    private val _pokemonTypes = MutableStateFlow<Map<String, String>>(emptyMap())
    val pokemonTypes: StateFlow<Map<String, String>> = _pokemonTypes.asStateFlow()

    init {
        viewModelScope.launch {
            val list = repository.fetchPokemonList()
            _pokemonList.value = list
            list.forEach { result ->
                launch {
                    val detail = repository.fetchPokemonDetail(result.name)
                    val primaryType = detail.types.firstOrNull()?.type?.name
                    if (primaryType != null) {
                        _pokemonTypes.update { it + (result.name to primaryType) }
                    }
                }
            }
        }
    }

    fun toggleFavorite(name: String) = repository.toggleFavorite(name)
    fun isFavorite(name: String) = repository.isFavorite(name)
}
