package com.halil.ozel.pokemonapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.PokemonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonResult>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResult>> = _pokemonList.asStateFlow()

    init {
        viewModelScope.launch {
            _pokemonList.value = repository.fetchPokemonList()
        }
    }

    fun toggleFavorite(name: String) = repository.toggleFavorite(name)
    fun isFavorite(name: String) = repository.isFavorite(name)
}
