package com.aigal.pokedax.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _sortOrder = MutableStateFlow("NumberAsc")
    val sortOrder: StateFlow<String> = _sortOrder

    val pokemonList: StateFlow<List<PokemonEntity>> =
        combine(
            repository.getPokemonList(),
            _sortOrder
        ) { pokemons, order ->
            sortPokemon(pokemons, order)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun setSortOrder(order: String) {
        _sortOrder.value = order
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