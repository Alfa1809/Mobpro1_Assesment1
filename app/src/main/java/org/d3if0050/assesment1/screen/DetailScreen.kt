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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
fun DetailScreen(navController: NavHostController) {

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var bill by rememberSaveable { mutableStateOf("") }
    var expense by rememberSaveable { mutableStateOf("") }
    var friendExpense by rememberSaveable { mutableStateOf("") }
    var whoPay by rememberSaveable { mutableStateOf(Pay.YOU.toString()) }
    var resultPay by rememberSaveable { mutableStateOf("") }

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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
    ) { padding ->
        FormSplitBill(
            name = name,
            onNameChange = { name = it },
            bill = bill,
            onBillChange = {
                bill = it

                if (bill == "" || bill == "0" ||
                    expense == "" || expense == "0" ||
                    friendExpense == "" || friendExpense == "0"
                ) return@FormSplitBill

                resultPay = whoPay(context, Pay.valueOf(whoPay), bill.toFloat(), expense.toFloat())

            },
            expense = expense,
            onExpenseChange = {
                expense = it
                friendExpense = (bill.toFloat() - it.toFloat()).toString()

                if (bill == "" || bill == "0" ||
                    expense == "" || expense == "0" ||
                    friendExpense == "" || friendExpense == "0"
                ) return@FormSplitBill

                resultPay = whoPay(context, Pay.valueOf(whoPay), bill.toFloat(), expense.toFloat())
            },
            friendExpense = friendExpense,
            whoPay = whoPay,
            resultPay = resultPay,
            onPayChange = {
                whoPay = it

                if (bill == "" || bill == "0" ||
                    expense == "" || expense == "0" ||
                    friendExpense == "" || friendExpense == "0"
                ) return@FormSplitBill

                resultPay = whoPay(context, Pay.valueOf(whoPay), bill.toFloat(), expense.toFloat())
            },
            context = context,
            modifier = Modifier.padding(padding)
        )
    }
}

enum class Pay {
    YOU,
    FRIEND
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSplitBill(
    name: String, onNameChange: (String) -> Unit,
    bill: String, onBillChange: (String) -> Unit,
    expense: String, onExpenseChange: (String) -> Unit,
    friendExpense: String, resultPay: String,
    whoPay: String, onPayChange: (String) -> Unit,
    context: Context,
    modifier: Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.split_a_bill_with_your_friend),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
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
                    Text(text = "🤵", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = stringResource(R.string.friend_name),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        onNameChange(it)
                    },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),

                    leadingIcon = {
                        Text(text = "Rp.")
                    },
                    placeholder = { Text(text = "0") },
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
                    Text(text = "💰", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = stringResource(R.string.bill_value),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedTextField(
                    value = bill,
                    onValueChange = {
                        onBillChange(it)
                    },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Text(text = "Rp.")
                    },
                    placeholder = { Text(text = "0") },
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
                    Text(
                        text = "Your Expense",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedTextField(
                    value = expense,
                    onValueChange = {
                        onExpenseChange(it)
                    },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Text(text = "Rp.")
                    },
                    placeholder = { Text(text = "0") },
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
                    Text(
                        text = stringResource(R.string.your_friend_expense),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedTextField(
                    value = friendExpense,
                    onValueChange = {},
                    modifier = Modifier.weight(2f),
                    readOnly = true,
                    placeholder = { Text(text = "0") },
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Text(text = "Rp.")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
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
                    Text(
                        text = "Who Is Paying \nThe Bill",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(2f)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = whoPay,
                        onValueChange = {},
                        trailingIcon = { TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface
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
                                    onPayChange(pay.toString())
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
                Button(
                    onClick = {

                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(text = "Reset")
                }
            }
        }

        if (resultPay != "") {
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = resultPay, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.size(10.dp))
                IconButton(onClick = {
                    shareData(
                        context,
                        whoPay(context, Pay.valueOf(whoPay), bill.toFloat(), expense.toFloat())
                    )
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

fun whoPay(context: Context, pay: Pay, bill: Float, expense: Float): String {
    val formatter = NumberFormat.getInstance(Locale("in", "ID"))
    formatter.currency = Currency.getInstance("IDR")

    return if (expense == bill - expense) {
        context.getString(
            R.string.result_same,
            formatter.format(bill),
            formatter.format(expense),
            formatter.format(bill - expense)
        )
    } else {
        when (pay) {
            Pay.FRIEND -> {
                context.getString(
                    R.string.result,
                    (bill).toString(),
                    (expense).toString(),
                    (bill - expense).toString(),
                    "your friend",
                    "you",
                    (bill - expense).toString()
                )
            }

            Pay.YOU -> {
                context.getString(
                    R.string.result,
                    (bill).toString(),
                    (expense).toString(),
                    formatter.format(bill - expense),
                    "you",
                    "your friend",
                    (bill - expense).toString()
                )
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

fun formatCurrency(amount: Float): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("IND", "ID"))
    formatter.currency = Currency.getInstance("IDR")
    return formatter.format(amount)
}

fun parseCurrency(currencyString: String): Float {
    val formatter =
        NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Use "id" instead of "IND"
    formatter.currency = Currency.getInstance("IDR")
    val parsedNumber = formatter.parse(currencyString)
    return parsedNumber?.toFloat() ?: 0f
}


@Preview(showBackground = true)
@Composable
fun PrevDetail() {
    DetailScreen(rememberNavController())
}