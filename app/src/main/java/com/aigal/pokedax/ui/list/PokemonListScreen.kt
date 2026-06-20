package com.aigal.pokedax.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aigal.pokedax.ui.list.components.PokemonCard
import com.aigal.pokedax.ui.list.components.SortDropdownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PokéAsh") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF0F0F0)),
                actions = {
                    SortDropdownMenu(
                        currentSort = sortOrder,
                        onSortSelected = { newOrder ->
                            viewModel.setSortOrder(newOrder)
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn {
                items(pokemonList) { pokemon ->
                    PokemonCard(pokemon = pokemon,
                        onClick = {
                            navController.navigate("pokemon_detail_screen/${pokemon.id}")
                        })
                }
            }
        }
    }
}