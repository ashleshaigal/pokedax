package com.aigal.pokedax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aigal.pokedax.ui.detail.PokemonDetailScreen
import com.aigal.pokedax.ui.list.PokemonListScreen
import com.aigal.pokedax.ui.theme.PokedaxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedaxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "pokemon_list_screen") {
                        composable("pokemon_list_screen") {
                            PokemonListScreen(
                                navController = navController
                            )
                        }
                        composable(
                            "pokemon_detail_screen/{pokemonId}",
                            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                        ) {
                            PokemonDetailScreen( navController = navController)
                        }
                    }
                }
            }
        }
    }
}