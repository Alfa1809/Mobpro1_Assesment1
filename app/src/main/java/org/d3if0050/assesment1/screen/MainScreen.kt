package org.d3if0050.assesment1.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0050.assesment1.R
import org.d3if0050.assesment1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.screenshot_2024_04_03_115020_removebg_preview),
                        contentDescription = "",
                        modifier = Modifier.size(80.dp)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor =  Color(0XffFDF4E3),
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = stringResource(R.string.tentang_aplikasi), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
    ) {padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier){

}

@Preview(showBackground = true)
@Composable
fun PrevMain(){
    MainScreen(rememberNavController())
}