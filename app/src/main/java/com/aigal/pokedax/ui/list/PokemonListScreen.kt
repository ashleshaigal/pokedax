package com.aigal.pokedax.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.ui.list.components.PokemonCard
import com.aigal.pokedax.ui.list.components.SortButton
import com.aigal.pokedax.ui.theme.PokedaxTheme

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    PokemonListContent(
        pokemonList = pokemonList,
        sortOrder = sortOrder,
        searchQuery = searchQuery,
        favoriteIds = favoriteIds,
        isLoggedIn = isLoggedIn,
        onSortSelected = { viewModel.setSortOrder(it) },
        onSearchQueryChange = { viewModel.setSearchQuery(it) },
        onFavoriteClick = { viewModel.toggleFavorite(it) },
        onPokemonClick = { navController.navigate("pokemon_detail_screen/${it.id}") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    pokemonList: List<PokemonEntity>,
    sortOrder: String,
    searchQuery: String,
    favoriteIds: Set<Int>,
    isLoggedIn: Boolean,
    onSortSelected: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFavoriteClick: (PokemonEntity) -> Unit,
    onPokemonClick: (PokemonEntity) -> Unit
) {
    val listState = rememberLazyListState()
    
    // Logic to hide/show search bar based on scroll direction
    var previousScrollOffset by remember { mutableIntStateOf(0) }
    var previousFirstVisibleItemIndex by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentFirstVisibleItemIndex = listState.firstVisibleItemIndex
        val currentScrollOffset = listState.firstVisibleItemScrollOffset

        if (currentFirstVisibleItemIndex > previousFirstVisibleItemIndex) {
            isVisible = false // Hide when scrolling down (moving towards bottom)
        } else if (currentFirstVisibleItemIndex < previousFirstVisibleItemIndex) {
            isVisible = true // Show when scrolling up (moving towards top)
        } else if (currentScrollOffset > previousScrollOffset) {
            if (currentFirstVisibleItemIndex > 0) {
                isVisible = false 
            }
        } else if (currentScrollOffset < previousScrollOffset) {
            isVisible = true
        }
        
        // Always show search bar when at the very top
        if (currentFirstVisibleItemIndex == 0 && currentScrollOffset == 0) {
            isVisible = true
        }
        
        previousFirstVisibleItemIndex = currentFirstVisibleItemIndex
        previousScrollOffset = currentScrollOffset
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    placeholder = { Text("Search Pokémon...", color = Color.Gray, fontSize = 14.sp) },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Search, 
                            contentDescription = null,
                            tint = Color.Gray
                        ) 
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(26.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F3F4),
                        unfocusedContainerColor = Color(0xFFF1F3F4),
                        disabledContainerColor = Color(0xFFF1F3F4),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))

                SortButton(
                    currentSort = sortOrder,
                    onSortSelected = onSortSelected
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(pokemonList) { pokemon ->
                PokemonCard(
                    pokemon = pokemon,
                    isFavorite = favoriteIds.contains(pokemon.id),
                    showFavoriteButton = isLoggedIn,
                    onFavoriteClick = { onFavoriteClick(pokemon) },
                    onClick = { onPokemonClick(pokemon) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview() {
    PokedaxTheme {
        PokemonListContent(
            pokemonList = listOf(
                PokemonEntity(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = listOf("Grass", "Poison"),
                    hp = 45, attack = 49, defense = 49, spAttack = 65, spDefense = 65, speed = 45,
                    species = "Seed Pokémon", description = "Bulbasaur..."
                ),
                PokemonEntity(
                    id = 4,
                    name = "Charmander",
                    imageUrl = "",
                    type = listOf("Fire"),
                    hp = 39, attack = 52, defense = 43, spAttack = 60, spDefense = 50, speed = 65,
                    species = "Lizard Pokémon", description = "Charmander..."
                )
            ),
            sortOrder = "NumberAsc",
            searchQuery = "",
            favoriteIds = setOf(1),
            isLoggedIn = true,
            onSortSelected = {},
            onSearchQueryChange = {},
            onFavoriteClick = {},
            onPokemonClick = {}
        )
    }
}
