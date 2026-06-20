package com.aigal.pokedax.domain.repository

import com.aigal.pokedax.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<PokemonEntity>>
    fun getPokemonDetails(id: Int): Flow<PokemonEntity>
}