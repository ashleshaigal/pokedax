package com.aigal.pokedax.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.ui.components.CommonTopBar
import com.aigal.pokedax.ui.list.PokemonListViewModel
import com.aigal.pokedax.ui.list.components.PokemonCard
import com.aigal.pokedax.ui.theme.PokedaxTheme

@Composable
fun SavedScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val savedPokemon = pokemonList.filter { favoriteIds.contains(it.id) }

    SavedScreenContent(
        savedPokemon = savedPokemon,
        isLoggedIn = isLoggedIn,
        onFavoriteClick = { viewModel.toggleFavorite(it) },
        onPokemonClick = { navController.navigate("pokemon_detail_screen/${it.id}") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreenContent(
    savedPokemon: List<PokemonEntity>,
    isLoggedIn: Boolean,
    onFavoriteClick: (PokemonEntity) -> Unit,
    onPokemonClick: (PokemonEntity) -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopBar(title = "Saved Pokémon")
        }
    ) { innerPadding ->
        if (!isLoggedIn) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(text = "Please log in to see your saved Pokémon")
            }
        } else if (savedPokemon.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(text = "No saved Pokémon yet!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(savedPokemon) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        isFavorite = true,
                        showFavoriteButton = true,
                        onFavoriteClick = { onFavoriteClick(pokemon) },
                        onClick = { onPokemonClick(pokemon) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedScreenPreview() {
    PokedaxTheme {
        SavedScreenContent(
            savedPokemon = listOf(
                PokemonEntity(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = listOf("Grass", "Poison"),
                    hp = 45, attack = 49, defense = 49, spAttack = 65, spDefense = 65, speed = 45,
                    species = "Seed Pokémon", description = "Bulbasaur..."
                )
            ),
            isLoggedIn = true,
            onFavoriteClick = {},
            onPokemonClick = {}
        )
    }
}
