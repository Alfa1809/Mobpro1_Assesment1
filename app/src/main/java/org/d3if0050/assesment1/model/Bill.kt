package org.d3if0050.assesment1.model

import java.util.Date

data class Bill (
    val name: String,
    val expense: Float,
    val friendExpense: Float,
    val totalBill: Float,
    val date: Date,
    val whoPay: String
)