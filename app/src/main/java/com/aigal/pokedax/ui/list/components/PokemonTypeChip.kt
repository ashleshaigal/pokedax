package com.aigal.pokedax.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aigal.pokedax.ui.theme.PokedaxTheme
import com.aigal.pokedax.ui.theme.TypeColors

@Composable
fun PokemonTypeChip(type: String) {
    Text(
        text = type,
        color = Color.White,
        modifier = Modifier
            .background(
                color = TypeColors.getColor(type),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PokemonTypeChipPreview() {
    PokedaxTheme {
        PokemonTypeChip(type = "Grass")
    }
}
