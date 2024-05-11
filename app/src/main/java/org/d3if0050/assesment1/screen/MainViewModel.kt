package org.d3if0050.assesment1.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0050.assesment1.database.BillDao
import org.d3if0050.assesment1.model.Bill

class MainViewModel(dao: BillDao) : ViewModel() {

    val data: StateFlow<List<Bill>> = dao.getOrder().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}