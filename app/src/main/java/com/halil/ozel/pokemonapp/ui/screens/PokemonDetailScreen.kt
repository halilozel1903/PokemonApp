package com.halil.ozel.pokemonapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.halil.ozel.pokemonapp.data.PokemonDetail
import com.halil.ozel.pokemonapp.data.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun PokemonDetailScreen(
    name: String,
    repository: PokemonRepository,
    onBack: () -> Unit = {}
) {
    val detail: PokemonDetail = runBlocking(Dispatchers.IO) {
        repository.fetchPokemonDetail(name)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            IconButton(
                onClick = { repository.toggleFavorite(detail.name) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                val icon = if (repository.isFavorite(detail.name)) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                Icon(imageVector = icon, contentDescription = null)
            }

            val imageUrl = detail.sprites.other?.officialArtwork?.frontDefault
                ?: detail.sprites.frontDefault
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${detail.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} #${detail.id}",
                    style = MaterialTheme.typography.headlineMedium
                )

                if (detail.types.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        detail.types.forEach { typeSlot ->
                            Card(shape = RoundedCornerShape(50)) {
                                Text(
                                    text = typeSlot.type.name,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = "About", style = MaterialTheme.typography.titleMedium)

                detail.height?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Height: $it")
                }
                detail.weight?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Weight: $it")
                }
                detail.baseExperience?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Base Exp: $it")
                }

                if (detail.stats.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(text = "Base Stats", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    detail.stats.forEach { stat ->
                        Text(text = stat.stat.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                        LinearProgressIndicator(
                            progress = (stat.baseStat.coerceAtMost(100)) / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
