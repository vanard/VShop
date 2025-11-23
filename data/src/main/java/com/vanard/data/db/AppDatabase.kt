package com.vanard.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vanard.data.dao.CartDao
import com.vanard.data.dao.ProductDao
import com.vanard.data.dao.UserDao
import com.vanard.data.entities.CartEntity
import com.vanard.data.entities.ProductEntity
import com.vanard.data.entities.UserEntity

@Database(
    entities = [ProductEntity::class, CartEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "shop_database.db")
                .fallbackToDestructiveMigration(true)
                .build()
    }

    abstract fun getProductDao(): ProductDao
    abstract fun getCartDao(): CartDao
    abstract fun getUserDao(): UserDao
}