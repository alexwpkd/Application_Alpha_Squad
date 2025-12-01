package com.example.myapplication.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.Model.ProductoEntity

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<ProductoEntity>)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): ProductoEntity?
}
