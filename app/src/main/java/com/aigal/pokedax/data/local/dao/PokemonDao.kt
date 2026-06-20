package com.aigal.pokedax.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aigal.pokedax.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_table")
    fun getPokemonList(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE id = :pokemonId")
    fun getPokemonById(pokemonId: Int): Flow<PokemonEntity>
}