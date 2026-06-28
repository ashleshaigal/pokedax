package com.aigal.pokedax.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.domain.repository.PokemonRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    
    private val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    private val _sortOrder = MutableStateFlow("NumberAsc")
    val sortOrder: StateFlow<String> = _sortOrder

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteIds: StateFlow<Set<Int>> = authStateFlow
        .flatMapLatest { user ->
            if (user != null) {
                repository.getFavoriteIds(user.uid)
            } else {
                flowOf(emptySet())
            }
        }
        .catch { emit(emptySet()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val isLoggedIn: StateFlow<Boolean> = authStateFlow
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), auth.currentUser != null)

    val pokemonList: StateFlow<List<PokemonEntity>> =
        combine(
            repository.getPokemonList(),
            _sortOrder,
            _searchQuery
        ) { pokemons, order, query ->
            val filtered = if (query.isBlank()) {
                pokemons
            } else {
                pokemons.filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.id.toString().contains(query)
                }
            }
            sortPokemon(filtered, order)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun toggleFavorite(pokemon: PokemonEntity) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.toggleFavorite(pokemon, userId)
        }
    }

    fun setSortOrder(order: String) {
        _sortOrder.value = order
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Move the sorting logic from the repository to the ViewModel
    private fun sortPokemon(list: List<PokemonEntity>, sortOrder: String): List<PokemonEntity> {
        return when (sortOrder) {
            "NumberAsc" -> list.sortedBy { it.id }
            "NumberDesc" -> list.sortedByDescending { it.id }
            "NameAsc" -> list.sortedBy { it.name }
            "NameDesc" -> list.sortedByDescending { it.name }
            else -> list.sortedBy { it.id }
        }
    }

    // You no longer need `getPokemonList()` in the `init` block.
    // The `combine` and `stateIn` operators handle the initial data loading.
}