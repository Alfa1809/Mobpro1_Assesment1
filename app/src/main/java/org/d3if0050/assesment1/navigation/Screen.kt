package org.d3if0050.assesment1.navigation

import org.d3if0050.assesment1.screen.KEY_ID_BILL

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_BILL}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}