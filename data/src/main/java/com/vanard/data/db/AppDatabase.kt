package com.vanard.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "shop_database.db")
                .fallbackToDestructiveMigration(false)
                .build()
    }

    abstract fun getProductDao(): ProductDao
    abstract fun getCartDao(): CartDao
}