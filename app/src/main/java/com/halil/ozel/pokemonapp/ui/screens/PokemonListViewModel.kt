package com.halil.ozel.pokemonapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.PokemonListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            repository.fetchPokemonList()
                .onSuccess { pokemonList ->
                    _uiState.update { 
                        it.copy(
                            pokemonList = pokemonList,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    
                    // Load types for each Pokemon
                    pokemonList.forEach { pokemon ->
                        launch {
                            repository.fetchPokemonDetail(pokemon.name)
                                .onSuccess { detail ->
                                    val primaryType = detail.types.firstOrNull()?.type?.name
                                    if (primaryType != null) {
                                        _uiState.update { currentState ->
                                            currentState.copy(
                                                pokemonTypes = currentState.pokemonTypes + (pokemon.name to primaryType)
                                            )
                                        }
                                    }
                                }
                                .onFailure { exception ->
                                    Log.w("PokemonListViewModel", "Failed to load type for ${pokemon.name}", exception)
                                }
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load Pokemon list: ${exception.message}"
                        )
                    }
                }
        }
    }

    fun retryLoadPokemon() {
        loadPokemonList()
    }

    fun toggleFavorite(name: String) = repository.toggleFavorite(name)
    
    fun isFavorite(name: String) = repository.isFavorite(name)

    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}
