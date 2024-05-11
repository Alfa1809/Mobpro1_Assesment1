package org.d3if0050.assesment1.screen

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0050.assesment1.database.BillDao
import org.d3if0050.assesment1.model.Bill
import java.util.Date

class DetailViewModel(private val dao: BillDao) : ViewModel() {

    fun insert(
         name: String,
         expense: Float,
         friendExpense: Float,
         totalBill: Float,
         @StringRes whoPay: Int
    ) {
        val bill = Bill(
            name = name,
            expense = expense,
            friendExpense = friendExpense,
            totalBill = totalBill,
            date = Date(),
            whoPay = whoPay,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(bill)
        }
    }

    suspend fun getOrder(id: Long): Bill? {
        return dao.getOrderById(id)
    }

    fun update(
        id: Long,
        name: String,
        expense: Float,
        friendExpense: Float,
        totalBill: Float,
        @StringRes whoPay: Int
    ) {
        val bill = Bill(
            id = id,
            name = name,
            expense = expense,
            friendExpense = friendExpense,
            totalBill = totalBill,
            date = Date(),
            whoPay = whoPay,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(bill)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}