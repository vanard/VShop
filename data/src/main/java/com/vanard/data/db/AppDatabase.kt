package com.vanard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vanard.data.dao.ProductDao
import com.vanard.data.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
}