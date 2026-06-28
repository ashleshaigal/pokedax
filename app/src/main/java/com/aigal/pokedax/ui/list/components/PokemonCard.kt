package com.aigal.pokedax.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.ui.theme.PokedaxTheme
import com.aigal.pokedax.ui.theme.TypeColors

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonCard(
    pokemon: PokemonEntity,
    isFavorite: Boolean = false,
    showFavoriteButton: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },

        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = pokemon.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "#${pokemon.id} ${pokemon.name}")
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    pokemon.type.forEach { type ->
                        Text(
                            text = type,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .background(TypeColors.getColor(type), shape = RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            if (showFavoriteButton) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonCardPreview() {
    PokedaxTheme {
        PokemonCard(
            pokemon = PokemonEntity(
                id = 1,
                name = "Bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                type = listOf("Grass", "Poison"),
                hp = 45,
                attack = 49,
                defense = 49,
                spAttack = 65,
                spDefense = 65,
                speed = 45,
                species = "Seed Pokémon",
                description = "Bulbasaur can be seen napping in bright sunlight. There is a seed on its back. By soaking up the sun's rays, the seed grows progressively larger."
            ),
            isFavorite = false,
            showFavoriteButton = true,
            onFavoriteClick = {},
            onClick = {}
        )
    }
}
