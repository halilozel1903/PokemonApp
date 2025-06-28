package com.halil.ozel.pokemonapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.halil.ozel.pokemonapp.R
import com.halil.ozel.pokemonapp.data.PokemonRepository
import com.halil.ozel.pokemonapp.data.PokemonResult

@Composable
fun PokemonListScreen(
    repository: PokemonRepository,
    onSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val pokemonList by produceState(initialValue = emptyList<PokemonResult>()) {
        value = repository.fetchPokemonList()
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            val filtered = if (searchQuery.text.isEmpty()) {
                pokemonList
            } else {
                pokemonList.filter { it.name.contains(searchQuery.text, ignoreCase = true) }
            }
            items(filtered) { pokemon ->
                PokemonRow(pokemon = pokemon, repository = repository) {
                    onSelected(pokemon.name)
                }
            }
        }
    }
}

@Composable
private fun PokemonRow(
    pokemon: PokemonResult,
    repository: PokemonRepository,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val id = pokemon.url.trimEnd('/').split("/").last()
        AsyncImage(
            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_launcher_foreground)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = { repository.toggleFavorite(pokemon.name) }) {
            val icon = if (repository.isFavorite(pokemon.name)) Icons.Default.Favorite else Icons.Default.FavoriteBorder
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}
