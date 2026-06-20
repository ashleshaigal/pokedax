package com.aigal.pokedax.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }
}