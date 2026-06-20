package com.aigal.pokedax.ui.theme

import androidx.compose.ui.graphics.Color

object TypeColors {
    fun getColor(type: String): Color {
        return when (type) {
            "Grass" -> Color(0xFF7AC74C)
            "Poison" -> Color(0xFFA33EA1)
            "Fire" -> Color(0xFFEE8130)
            "Flying" -> Color(0xFFA98FF3)
            "Water" -> Color(0xFF6390F0)
            "Bug" -> Color(0xFFA6B91A)
            "Normal" -> Color(0xFFA8A77A)
            "Electric" -> Color(0xFFF7D02C)
            "Ground" -> Color(0xFFE2BF65)
            "Fairy" -> Color(0xFFDDA6F8)
            "Fighting" -> Color(0xFFC22E28)
            "Psychic" -> Color(0xFFF95587)
            "Rock" -> Color(0xFFB6A136)
            "Ghost" -> Color(0xFF735797)
            "Ice" -> Color(0xFF96D9D6)
            "Dragon" -> Color(0xFF6F35FC)
            "Steel" -> Color(0xFFB7B7CE)
            "Dark" -> Color(0xFF705746)
            else -> Color.Gray
        }
    }
}