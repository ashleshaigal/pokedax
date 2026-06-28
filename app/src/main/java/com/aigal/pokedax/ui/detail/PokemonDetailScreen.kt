package com.aigal.pokedax.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aigal.pokedax.ui.components.CommonTopBar
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.ui.list.components.PokemonTypeChip
import com.aigal.pokedax.ui.theme.PokedaxTheme

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemon by viewModel.pokemonDetails.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = pokemon?.name ?: "Pokémon Details",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            pokemon?.let { poke ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(text = "#${poke.id} ${poke.name}", fontSize = 32.sp)
                    }

                    item {
                        GlideImage(
                            model = poke.imageUrl,
                            contentDescription = poke.name,
                            modifier = Modifier.size(240.dp)
                        )
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            poke.type.forEach { type ->
                                PokemonTypeChip(type = type)
                            }
                        }
                    }

                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = poke.species ?: "N/A", fontSize = 20.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = poke.description ?: "No description available.",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    item {
                        BaseStatsSection(pokemon = poke)
                    }
                    
                    // Extra spacer at bottom to ensure stats are fully visible
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BaseStatsSection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Base Stats", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))

        StatRow(label = "HP", value = pokemon.hp)
        StatRow(label = "Attack", value = pokemon.attack)
        StatRow(label = "Defense", value = pokemon.defense)
        StatRow(label = "Sp. Attack", value = pokemon.spAttack)
        StatRow(label = "Sp. Defense", value = pokemon.spDefense)
        StatRow(label = "Speed", value = pokemon.speed)
    }
}

@Preview(showBackground = true)
@Composable
fun BaseStatsSectionPreview() {
    PokedaxTheme {
        BaseStatsSection(
            pokemon = PokemonEntity(
                id = 1,
                name = "Bulbasaur",
                imageUrl = "",
                type = listOf("Grass", "Poison"),
                hp = 45,
                attack = 49,
                defense = 49,
                spAttack = 65,
                spDefense = 65,
                speed = 45,
                species = "Seed Pokémon",
                description = "Bulbasaur..."
            )
        )
    }
}

@Composable
fun StatRow(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(120.dp))
        Text(text = value.toString(), modifier = Modifier.width(40.dp))
        LinearProgressIndicator(
            progress = { value / 255f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.LightGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatRowPreview() {
    PokedaxTheme {
        StatRow(label = "HP", value = 45)
    }
}
