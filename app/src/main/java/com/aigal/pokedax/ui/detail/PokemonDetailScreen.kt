package com.aigal.pokedax.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        }
    ) { innerPadding ->
        pokemon?.let { poke ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = "#${poke.id} ${poke.name}", fontSize = 32.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    GlideImage(
                        model = poke.imageUrl,
                        contentDescription = poke.name,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        poke.type.forEach { type ->
                            PokemonTypeChip(type = type)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = poke.species ?: "N/A", fontSize = 20.sp, color = Color.Gray)
                    Text(text = poke.description ?: "No description available.", modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    BaseStatsSection(pokemon = poke)
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
        Spacer(modifier = Modifier.height(8.dp))

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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(120.dp))
        Text(text = value.toString(), modifier = Modifier.width(40.dp))
        // Placeholder for the progress bar
    }
}

@Preview(showBackground = true)
@Composable
fun StatRowPreview() {
    PokedaxTheme {
        StatRow(label = "HP", value = 45)
    }
}
