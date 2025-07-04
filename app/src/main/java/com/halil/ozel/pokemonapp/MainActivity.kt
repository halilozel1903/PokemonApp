package com.halil.ozel.pokemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.halil.ozel.pokemonapp.ui.screens.PokemonDetailScreen
import com.halil.ozel.pokemonapp.ui.screens.PokemonListScreen
import com.halil.ozel.pokemonapp.ui.theme.PokemonAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PokemonApp()
                }
            }
        }
    }
}

@Composable
fun PokemonApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            PokemonListScreen(onSelected = { name ->
                navController.navigate("detail/$name")
            })
        }
        composable(
            "detail/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: return@composable
            PokemonDetailScreen(
                name = name,
                onBack = { navController.popBackStack() },
                onEvolutionClick = { navController.navigate("detail/$it") }
            )
        }
    }
}
