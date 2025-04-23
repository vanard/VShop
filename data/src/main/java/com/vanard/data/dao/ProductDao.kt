package com.vanard.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vanard.data.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(listProductEntity: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products_item SET quantityInCart = quantityInCart + 1 WHERE id = :id")
    suspend fun increaseQuantity(id: Long)

    @Query("UPDATE products_item SET quantityInCart = quantityInCart - 1 WHERE id = :id AND quantityInCart > 1")
    suspend fun decreaseQuantity(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM products_item WHERE id = :id AND quantityInCart > 0)")
    suspend fun getProductInChartExist(id: Long): Boolean

    @Query("SELECT * FROM products_item WHERE quantityInCart > 0")
    fun getAllProductInChart(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products_item WHERE id = :id")
    fun getProductById(id:Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products_item")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products_item WHERE isFavorite = 1")
    fun getAllFavoriteProducts(): Flow<List<ProductEntity>>

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("DELETE FROM products_item")
    suspend fun deleteAllProducts()

}