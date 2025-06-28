package com.halil.ozel.pokemonapp.ui.theme

import androidx.compose.ui.graphics.Color

fun getColorFromType(type: String): Color {
    return when (type.lowercase()) {
        "bug" -> Color(0xFF8BD674)
        "dark" -> Color(0xFF6F6E78)
        "dragon" -> Color(0xFF7383B9)
        "electric" -> Color(0xFFFFD86F)
        "fairy" -> Color(0xFFEBA8C3)
        "fighting" -> Color(0xFFEB4971)
        "fire" -> Color(0xFFFB6C6C)
        "flying" -> Color(0xFF83A2E3)
        "ghost" -> Color(0xFF8571BE)
        "grass" -> Color(0xFF48D0B0)
        "ground" -> Color(0xFFF78551)
        "ice" -> Color(0xFF91D8DF)
        "normal" -> Color(0xFFB5B9C4)
        "poison" -> Color(0xFF9F6E97)
        "psychic" -> Color(0xFFFF6568)
        "rock" -> Color(0xFFD4C294)
        "steel" -> Color(0xFF4C91B2)
        "water" -> Color(0xFF76BDFE)
        else -> Color.Black
    }
}
