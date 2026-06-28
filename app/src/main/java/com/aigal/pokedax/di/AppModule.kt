package com.aigal.pokedax.di

import android.content.Context
import androidx.room.Room
import com.aigal.pokedax.data.local.PokemonDatabase
import com.aigal.pokedax.data.local.dao.PokemonDao
import com.aigal.pokedax.data.remote.PokemonApi
import com.aigal.pokedax.domain.repository.PokemonRepository
import com.aigal.pokedax.data.repository.PokemonRepositoryImpl
import com.aigal.pokedax.util.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApi {
        return retrofit.create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokemonApi,
        dao: PokemonDao,
        firestore: FirebaseFirestore
    ): PokemonRepository {
        return PokemonRepositoryImpl(api, dao, firestore)
    }
}