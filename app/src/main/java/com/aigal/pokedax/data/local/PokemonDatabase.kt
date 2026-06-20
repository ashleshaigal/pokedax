package com.aigal.pokedax.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aigal.pokedax.data.local.dao.PokemonDao
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.util.ListTypeConverter

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListTypeConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}