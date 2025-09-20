package com.halil.ozel.pokemonapp.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.PokemonDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val name: String = checkNotNull(savedStateHandle["name"])

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        loadPokemonDetail()
    }

    private fun loadPokemonDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // Load Pokemon detail
            launch {
                repository.fetchPokemonDetail(name)
                    .onSuccess { detail ->
                        _uiState.update { 
                            it.copy(
                                pokemon = detail,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { exception ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = "Failed to load Pokemon details: ${exception.message}"
                            )
                        }
                    }
            }
            
            // Load evolution data
            launch {
                repository.fetchEvolutionNames(name)
                    .onSuccess { evolutions ->
                        _uiState.update { 
                            it.copy(evolutions = evolutions)
                        }
                    }
                    .onFailure { exception ->
                        // Evolution data failure is not critical, just log it
                        android.util.Log.w("PokemonDetailViewModel", "Failed to load evolution data for $name", exception)
                    }
            }
        }
    }

    fun retryLoadPokemon() {
        loadPokemonDetail()
    }

    fun toggleFavorite() = repository.toggleFavorite(name)
    
    fun isFavorite() = repository.isFavorite(name)
}
