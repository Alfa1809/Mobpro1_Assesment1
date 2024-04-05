package org.d3if0050.assesment1.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0050.assesment1.R
import org.d3if0050.assesment1.navigation.Screen
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

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

enum class Pay{
    YOU,
    FRIEND
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier){
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf(Pay.YOU) }

    var bill by rememberSaveable {
        mutableStateOf("")
    }
    var billError by rememberSaveable {
        mutableStateOf(false)
    }

    var yourExpense by rememberSaveable {
        mutableStateOf("")
    }
    var yourExpenseError by rememberSaveable {
        mutableStateOf(false)
    }

    var friendExpense by rememberSaveable {
        mutableStateOf("")
    }


    var whoPay by rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Split A Bill With Your Friend",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
            )
        Column(
            modifier = Modifier
                .background(
                    Color(0XffFDF4E3),
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
               modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💰", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Bill Value", style = MaterialTheme.typography.labelLarge)
                }
                OutlinedTextField(
                    value = bill,
                    onValueChange = {
                                    bill = it
                    },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    supportingText = { ErrorHint(billError, "Bill") },
                    isError = billError,
                    leadingIcon = {
                        IconPicker(billError, "Rp.")
                    },
                    placeholder = { Text(text = "0" ) },
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }

Row(
               modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🕴️", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Your Expense", style = MaterialTheme.typography.labelLarge)
                }
    OutlinedTextField(
        value = yourExpense,
        onValueChange = {
            yourExpense = it
            if(bill.toFloat() > 0f && yourExpense.toFloat() != 0f){
                friendExpense = (bill.toFloat() - it.toFloat()).toString()
            }
        },
        supportingText = { ErrorHint(yourExpenseError, "Your Expense") },
        modifier = Modifier.weight(2f),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        isError = yourExpenseError,
        leadingIcon = {
            IconPicker(yourExpenseError, "Rp.")
        },
        placeholder = { Text(text = "0" ) },
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
            }

Row(
               modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🧑‍🤝‍🧑", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Your Friend Expense", style = MaterialTheme.typography.labelLarge)
                }
    OutlinedTextField(
        value = friendExpense,
        onValueChange = {},
        modifier = Modifier.weight(2f),
        readOnly = true,
        placeholder = { Text(text = "0" ) },
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            Text(text = "Rp.")
        },
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
    )
            }

Row(
               modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🤑", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Who Is Paying \nThe Bill", style = MaterialTheme.typography.labelLarge)
                }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.weight(2f)
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText.toString(),
            onValueChange = {},
            trailingIcon = { TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Pay.entries.forEach { pay ->
                DropdownMenuItem(
                    text = { Text(pay.toString()) },
                    onClick = {
                        selectedOptionText = pay
                        expanded = false
                    },
                )
            }
        }
    }
            }
           Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp)
           ) {
               Button(onClick = {
                   billError = (bill == "" || bill == "0")
                   yourExpenseError = (yourExpense == "" || yourExpense == "0")
                  if(billError && yourExpenseError) return@Button
                   whoPay = whoPay(context, selectedOptionText ,bill.toFloat(), yourExpense.toFloat())
               },
                   modifier = Modifier.weight(1f),
               ) {
                   Text(text = "Pay")
               }

               Button(onClick = {
                  bill = ""
                   yourExpense = ""
                   friendExpense = ""
                   whoPay = ""
                   selectedOptionText = Pay.FRIEND
                   billError = false
                   yourExpenseError = false
               },
                   modifier = Modifier.weight(1f),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color.Gray,
                   )
               ) {
                   Text(text = "Reset")
               }
           }
        }

       if(whoPay != ""){
           Column(
               modifier = Modifier
                   .background(
                       Color(0XffFDF4E3),
                       RoundedCornerShape(10.dp)
                   )
                   .fillMaxWidth()
                   .padding(24.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp)
           ){
               Text(text = whoPay)
               Spacer(modifier = Modifier.size(10.dp))
               IconButton(onClick = {
                    shareData(context, whoPay(context, selectedOptionText ,bill.toFloat(), yourExpense.toFloat()))
               }, modifier = Modifier.fillMaxWidth()) {
                   Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(R.string.share))
               }
           }
       }
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean, nameError: String) {
    if (isError) {
        Text(text = stringResource(id = R.string.input_invalid, nameError))
    }
}

fun whoPay(context: Context, pay: Pay, bill: Float, expense: Float): String {
    val formatter = NumberFormat.getInstance(Locale("in", "ID"))
    formatter.currency = Currency.getInstance("IDR")

    return if (expense == bill - expense) {
        context.getString(R.string.result_same, formatter.format(bill), formatter.format(expense), formatter.format(bill - expense))
    } else {
        when (pay) {
            Pay.FRIEND -> {
                context.getString(R.string.result, (bill).toString(), (expense).toString(), (bill - expense).toString(), "your friend", "you",  (bill - expense).toString())
            }
            Pay.YOU -> {
                context.getString(R.string.result, (bill).toString(), (expense).toString(), formatter.format(bill - expense), "you", "your friend", (bill - expense).toString())
            }
        }
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}


@Preview(showBackground = true)
@Composable
fun PrevMain(){
    MainScreen(rememberNavController())
}