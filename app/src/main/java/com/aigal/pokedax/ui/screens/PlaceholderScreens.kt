package com.aigal.pokedax.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aigal.pokedax.ui.theme.PokedaxTheme

@Composable
fun PlaceholderSavedScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Saved Screen - No Pokémon saved yet!")
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceholderSavedScreenPreview() {
    PokedaxTheme {
        PlaceholderSavedScreen()
    }
}

