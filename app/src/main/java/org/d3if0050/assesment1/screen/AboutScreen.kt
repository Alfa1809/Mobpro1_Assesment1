package org.d3if0050.assesment1.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3if0050.assesment1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.tentang_aplikasi))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0XffFDF4E3),
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) {padding ->
       Column(
           modifier = Modifier
               .padding(padding)
               .padding(16.dp)
               .background(
                   Color(0XffFDF4E3),
                   RoundedCornerShape(10.dp)
               )
               .fillMaxWidth()
               .padding(24.dp)
       ) {
            Image(painter = painterResource(id = R.drawable.whatsapp_image_2024_04_05_at_9_50_44_am), contentDescription = stringResource(
                R.string.identitas_foto
            ),
                Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally), contentScale = ContentScale.Crop)
           Spacer(modifier = Modifier.size(15.dp))
           Text(text = stringResource(R.string.ahmad_faza_alfarisi), style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
           Spacer(modifier = Modifier.size(15.dp))
           Text(text = stringResource(R.string.Nim), style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
           Spacer(modifier = Modifier.size(15.dp))
           Text(text = stringResource(id = R.string.eat_split_description))
       }
    }
}