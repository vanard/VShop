package com.vanard.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vanard.data.entities.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCart(listCartEntity: List<CartEntity>)

    @Query("SELECT * FROM carts_item WHERE id = :id")
    fun getCartById(id: Long): Flow<CartEntity?>

//    @Query("SELECT * FROM carts_item WHERE uuid = :uuid")
//    fun getCartByUUID(uuid: String): Flow<CartEntity?>

    @Query("SELECT * FROM carts_item")
    fun getAllCarts(): Flow<List<CartEntity>>

    @Update
    suspend fun updateCartItem(cart: CartEntity)

    @Delete
    suspend fun deleteCart(productEntity: CartEntity)

    @Query("DELETE FROM carts_item")
    suspend fun deleteAllCarts()

}