package com.aigal.pokedax.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aigal.pokedax.data.local.entity.PokemonEntity
import com.aigal.pokedax.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    private val _pokemonDetails = MutableStateFlow<PokemonEntity?>(null)
    val pokemonDetails: StateFlow<PokemonEntity?> = _pokemonDetails

    init {
        getPokemonDetails()
    }

    private fun getPokemonDetails() {
        viewModelScope.launch {
            repository.getPokemonDetails(pokemonId).collect { pokemon ->
                _pokemonDetails.value = pokemon
            }
        }
    }
}