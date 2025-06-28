package com.halil.ozel.pokemonapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.halil.ozel.pokemonapp.R
import com.halil.ozel.pokemonapp.data.ApiConstants
import com.halil.ozel.pokemonapp.data.PokemonResult
import org.koin.androidx.compose.koinViewModel

private enum class SortOption(val icon: ImageVector, val label: String) {
    A_Z(Icons.Default.ArrowUpward, "A-Z"),
    Z_A(Icons.Default.ArrowDownward, "Z-A"),
    POWER(Icons.Default.Bolt, "Power")
}

@Composable
fun PokemonListScreen(
    onSelected: (String) -> Unit,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortOption by remember { mutableStateOf<SortOption?>(null) }
    var savedSortOption by remember { mutableStateOf<SortOption?>(null) }
    val pokemonList by viewModel.pokemonList.collectAsState()

    // Clear sort option when searching, restore when search text is cleared
    LaunchedEffect(searchQuery.text) {
        if (searchQuery.text.isNotEmpty()) {
            if (sortOption != null) {
                savedSortOption = sortOption
                sortOption = null
            }
        } else {
            if (savedSortOption != null) {
                sortOption = savedSortOption
                savedSortOption = null
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        if (searchQuery.text.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SortOption.entries.forEach { option ->
                    Button(onClick = { sortOption = option }) {
                        Icon(option.icon, contentDescription = option.label)
                        Spacer(Modifier.width(4.dp))
                        Text(option.label)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        val filtered = if (searchQuery.text.isEmpty()) {
            pokemonList
        } else {
            pokemonList.filter { it.name.contains(searchQuery.text, ignoreCase = true) }
        }
        val sorted = when (sortOption) {
            SortOption.A_Z -> filtered.sortedBy { it.name }
            SortOption.Z_A -> filtered.sortedByDescending { it.name }
            SortOption.POWER -> filtered.sortedByDescending {
                it.url.trimEnd('/').split("/").last().toIntOrNull() ?: 0
            }
            null -> filtered
        }
        if (sorted.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.content_not_found))
                Spacer(Modifier.height(16.dp))
                AsyncImage(
                    model = "https://images.ctfassets.net/w5r1fvmogo3f/4VN7t0SD1XbEyy3KnJrHTy/7c4ebf24c325bf2f75fa07ab3b41f400/pokemon-banner_b40d63371a1542f8849329421436a7bf.jpeg?fm=webp&q=90&fit=scale&w=1920",
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(sorted) { pokemon ->
                    PokemonGridItem(pokemon = pokemon, viewModel = viewModel) {
                        onSelected(pokemon.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonGridItem(
    pokemon: PokemonResult,
    viewModel: PokemonListViewModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
            val id = pokemon.url.trimEnd('/').split("/").last()
            AsyncImage(
                model = "${ApiConstants.SPRITE_BASE_URL}/$id.png",
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_foreground)
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                val context = LocalContext.current
                IconButton(onClick = {
                    viewModel.toggleFavorite(pokemon.name)
                    val messageRes = if (viewModel.isFavorite(pokemon.name)) {
                        R.string.added_to_favorites
                    } else {
                        R.string.removed_from_favorites
                    }
                    Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
                }) {
                    val icon = if (viewModel.isFavorite(pokemon.name)) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }
    }
}
