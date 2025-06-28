package com.halil.ozel.pokemonapp.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.ozel.pokemonapp.data.PokemonDetail
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.EvolutionPokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val name: String = checkNotNull(savedStateHandle["name"])

    private val _detail = MutableStateFlow<PokemonDetail?>(null)
    val detail: StateFlow<PokemonDetail?> = _detail.asStateFlow()

    private val _evolutions = MutableStateFlow<List<EvolutionPokemon>>(emptyList())
    val evolutions: StateFlow<List<EvolutionPokemon>> = _evolutions.asStateFlow()

    init {
        viewModelScope.launch {
            _detail.value = repository.fetchPokemonDetail(name)
            _evolutions.value = repository.fetchEvolutionNames(name)
        }
    }

    fun toggleFavorite() = repository.toggleFavorite(name)
    fun isFavorite() = repository.isFavorite(name)
}
