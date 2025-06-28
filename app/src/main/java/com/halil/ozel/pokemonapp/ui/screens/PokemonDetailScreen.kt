package com.halil.ozel.pokemonapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.halil.ozel.pokemonapp.data.PokemonDetail
import com.halil.ozel.pokemonapp.data.EvolutionPokemon
import com.halil.ozel.pokemonapp.R
import org.koin.androidx.compose.koinViewModel
import com.halil.ozel.pokemonapp.ui.theme.getColorFromType
import com.halil.ozel.pokemonapp.data.ApiConstants

@Composable
fun PokemonDetailScreen(
    name: String,
    onBack: () -> Unit = {},
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val detailState by viewModel.detail.collectAsState()
    val evolutions by viewModel.evolutions.collectAsState()
    if (detailState == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    val detail: PokemonDetail = detailState!!

    val typeColor = detail.types.firstOrNull()?.let { getColorFromType(it.type.name) }
        ?: MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(typeColor)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            val context = LocalContext.current
            IconButton(
                onClick = {
                    viewModel.toggleFavorite()
                    val messageRes = if (viewModel.isFavorite()) {
                        R.string.added_to_favorites
                    } else {
                        R.string.removed_from_favorites
                    }
                    Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                val icon = if (viewModel.isFavorite()) Icons.Default.Favorite else Icons.Default.FavoriteBorder
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "${detail.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} #${detail.id}",
                    style = MaterialTheme.typography.headlineMedium
                )

                if (detail.types.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        detail.types.forEach { typeSlot ->
                            val color = getColorFromType(typeSlot.type.name)
                            Card(
                                shape = RoundedCornerShape(50),
                                colors = CardDefaults.cardColors(containerColor = color)
                            ) {
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
                Text(text = stringResource(R.string.about), style = MaterialTheme.typography.titleMedium)

                detail.height?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = stringResource(R.string.height_format, it))
                }
                detail.weight?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = stringResource(R.string.weight_format, it))
                }
                detail.baseExperience?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(text = stringResource(R.string.base_experience_format, it))
                }

                if (detail.stats.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(text = stringResource(R.string.base_stats), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    detail.stats.forEach { stat ->
                        Text(text = stat.stat.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                        LinearProgressIndicator(
                            progress = {
                                (stat.baseStat.coerceAtMost(100)) / 100f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (evolutions.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(text = stringResource(R.string.evolutions), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        evolutions.forEach { evo ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AsyncImage(
                                    model = "${ApiConstants.SPRITE_BASE_URL}/${evo.id}.png",
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Text(evo.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                            }
                        }
                    }
                }
            }
        }
    }
}
