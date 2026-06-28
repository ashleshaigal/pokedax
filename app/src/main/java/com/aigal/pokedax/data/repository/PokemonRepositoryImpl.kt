package com.aigal.pokedax.data.repository

import android.util.Log
import com.aigal.pokedax.data.local.dao.PokemonDao
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.data.remote.PokemonApi
import com.aigal.pokedax.domain.repository.PokemonRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    private val dao: PokemonDao,
    private val firestore: FirebaseFirestore
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

    override suspend fun toggleFavorite(pokemon: PokemonEntity, userId: String) {
        val docRef = firestore.collection("users").document(userId)
            .collection("favorites").document(pokemon.id.toString())

        val doc = docRef.get().await()
        if (doc.exists()) {
            docRef.delete().await()
        } else {
            docRef.set(pokemon).await()
        }
    }

    override fun getFavoriteIds(userId: String): Flow<Set<Int>> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .collection("favorites")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val ids = snapshot?.documents?.mapNotNull { it.id.toIntOrNull() }?.toSet() ?: emptySet()
                trySend(ids)
            }
        awaitClose { listener.remove() }
    }
}