package com.voitto.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }
    
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }
    
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.filter { it.isNotEmpty() }
    }
    
    @TypeConverter
    fun fromFloatPair(value: Pair<Float, Float>?): String? {
        return value?.let { "${it.first},${it.second}" }
    }
    
    @TypeConverter
    fun toFloatPair(value: String?): Pair<Float, Float>? {
        return value?.split(",")?.let { parts ->
            if (parts.size == 2) {
                Pair(parts[0].toFloat(), parts[1].toFloat())
            } else null
        }
    }
    
    @TypeConverter
    fun fromStringMap(value: Map<String, Float>?): String? {
        return value?.entries?.joinToString(";") { "${it.key}=${it.value}" }
    }
    
    @TypeConverter
    fun toStringMap(value: String?): Map<String, Float>? {
        if (value.isNullOrEmpty()) return null
        
        return try {
            value.split(";").associate { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) {
                    parts[0] to parts[1].toFloat()
                } else {
                    throw IllegalArgumentException("Invalid pair format: $pair")
                }
            }
        } catch (e: Exception) {
            // Return empty map on parsing error to prevent crashes
            emptyMap()
        }
    }
}
