package org.d3if0050.assesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0050.assesment1.model.Bill


@Dao
interface BillDao {
    @Insert
    suspend fun insert(bill: Bill)

    @Update
    suspend fun update(bill: Bill)

    @Query("SELECT * FROM `bill` ORDER BY date DESC")
    fun getOrder(): Flow<List<Bill>>

    @Query("SELECT * FROM `bill` WHERE id = :id")
    suspend fun getOrderById(id: Long): Bill?

    @Query("DELETE FROM `bill` WHERE id = :id")
    suspend fun deleteById(id: Long)
}