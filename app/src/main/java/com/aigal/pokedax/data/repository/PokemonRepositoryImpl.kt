package com.aigal.pokedax.data.repository

import android.util.Log
import com.aigal.pokedax.data.local.dao.PokemonDao
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.data.remote.PokemonApi
import com.aigal.pokedax.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    private val dao: PokemonDao
) : PokemonRepository {

    // Remove the sortOrder parameter
    override fun getPokemonList(): Flow<List<PokemonEntity>> {
        return dao.getPokemonList()
            .onEach { cachedList ->
                if (cachedList.isEmpty()) {
                    try {
                        val networkList = api.getPokemonList()
                        val entities = networkList.mapNotNull { pokemon ->
                            if (pokemon?.name == null || pokemon.image?.hires == null) {
                                Log.w("PokemonRepositoryImpl", "Skipping malformed pokemon with null essential data.")
                                return@mapNotNull null
                            }
                            PokemonEntity(
                                id = pokemon.id,
                                name = pokemon.name.english,
                                imageUrl = pokemon.image.hires,
                                type = pokemon.type,
                                hp = pokemon.base?.hp ?: 0,
                                attack = pokemon.base?.attack ?: 0,
                                defense = pokemon.base?.defense ?: 0,
                                spAttack = pokemon.base?.spAttack ?: 0,
                                spDefense = pokemon.base?.spDefense ?: 0,
                                speed = pokemon.base?.speed ?: 0,
                                species = pokemon.species ?: "N/A",
                                description = pokemon.description ?: "No description available."
                            )
                        }
                        dao.insertPokemon(entities)
                    } catch (e: Exception) {
                        Log.e("PokemonRepositoryImpl", "Network call failed: ${e.message}")
                    }
                }
            }
    }

    override fun getPokemonDetails(id: Int): Flow<PokemonEntity> {
        return dao.getPokemonById(id)
    }

    // Remove the sortPokemon method as it's no longer needed here
}