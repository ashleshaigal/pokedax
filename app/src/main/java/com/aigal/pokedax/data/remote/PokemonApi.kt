package com.aigal.pokedax.data.remote

import com.aigal.pokedax.data.remote.model.Pokemon
import retrofit2.http.GET

interface PokemonApi {

    @GET("pokedex.json")
    suspend fun getPokemonList(): List<Pokemon>
}