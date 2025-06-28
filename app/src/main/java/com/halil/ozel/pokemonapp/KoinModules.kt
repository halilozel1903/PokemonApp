package com.halil.ozel.pokemonapp

import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.ui.screens.PokemonDetailViewModel
import com.halil.ozel.pokemonapp.ui.screens.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PokemonRepository() }
    viewModel { PokemonListViewModel(get()) }
    viewModel { PokemonDetailViewModel(get(), get()) }
}
