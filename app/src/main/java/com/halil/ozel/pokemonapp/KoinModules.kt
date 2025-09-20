package com.halil.ozel.pokemonapp

import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.ui.screens.PokemonDetailViewModel
import com.halil.ozel.pokemonapp.ui.screens.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::PokemonRepository)
    viewModelOf(::PokemonListViewModel)
    viewModelOf(::PokemonDetailViewModel)
}
