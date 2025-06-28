package com.halil.ozel.pokemonapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Favorite
import androidx.compose.material3.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.halil.ozel.pokemonapp.data.PokemonDetail
import com.halil.ozel.pokemonapp.data.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Composable
fun PokemonDetailScreen(name: String, repository: PokemonRepository) {
    val detail: PokemonDetail = runBlocking(Dispatchers.IO) {
        repository.fetchPokemonDetail(name)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = detail.sprites.other?.officialArtwork?.frontDefault
            ?: detail.sprites.frontDefault
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = detail.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { repository.toggleFavorite(detail.name) }) {
                val icon = if (repository.isFavorite(detail.name)) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                Icon(imageVector = icon, contentDescription = null)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(text = "ID: ${detail.id}", style = MaterialTheme.typography.bodyLarge)
        detail.height?.let {
            Spacer(Modifier.height(4.dp))
            Text(text = "Height: $it", style = MaterialTheme.typography.bodyLarge)
        }
        detail.weight?.let {
            Spacer(Modifier.height(4.dp))
            Text(text = "Weight: $it", style = MaterialTheme.typography.bodyLarge)
        }
        detail.baseExperience?.let {
            Spacer(Modifier.height(4.dp))
            Text(text = "Base Exp: $it", style = MaterialTheme.typography.bodyLarge)
        }
        if (detail.types.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            val types = detail.types.joinToString { it.type.name }
            Text(text = "Types: $types", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
