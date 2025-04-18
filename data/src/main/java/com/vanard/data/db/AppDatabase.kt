package com.vanard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vanard.data.dao.CartDao
import com.vanard.data.dao.ProductDao
import com.vanard.data.entities.CartEntity
import com.vanard.data.entities.ProductEntity

@Database(
    entities = [ProductEntity::class, CartEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
    abstract fun CartDao(): CartDao
}