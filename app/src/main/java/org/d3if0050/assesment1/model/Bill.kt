package org.d3if0050.assesment1.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bill")
data class Bill (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val expense: Float,
    val friendExpense: Float,
    val totalBill: Float,
    val date: Date,
    @StringRes val whoPay: Int
)