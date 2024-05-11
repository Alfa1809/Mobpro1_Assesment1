package org.d3if0050.assesment1.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0050.assesment1.R
import org.d3if0050.assesment1.database.BillDb
import org.d3if0050.assesment1.util.ViewModelFactory
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    val context = LocalContext.current
    val db = BillDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var name by rememberSaveable { mutableStateOf("") }
    var bill by rememberSaveable { mutableStateOf("") }
    var expense by rememberSaveable { mutableStateOf("") }
    var friendExpense by rememberSaveable { mutableStateOf("") }
    var whoPay by rememberSaveable { mutableIntStateOf(R.string.me) }
    var resultPay by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getOrder(id) ?: return@LaunchedEffect
        name = data.name
        bill = data.totalBill.absoluteValue.toInt().toString()
        expense = data.expense.absoluteValue.toInt().toString()
        friendExpense = data.friendExpense.toInt().toString()
        whoPay = data.whoPay
        resultPay = whoPay(context, whoPay, bill.toFloat(), expense.toFloat())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.tambah_bill))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {

                        if (name == "" || name == "0"
                            || bill == "" || bill == "0" || toFloatorZero(bill) < toFloatorZero(expense)
                            || expense == "" || expense == "0" || toFloatorZero(expense) > toFloatorZero(bill)
                            || friendExpense == "" || friendExpense == "0"
                        ) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_cant_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(
                                name,
                                expense.toFloat(),
                                friendExpense.toFloat(),
                                bill.toFloat(),
                                whoPay
                            )
                        } else {
                            viewModel.update(
                                id,
                                name,
                                expense.toFloat(),
                                friendExpense.toFloat(),
                                bill.toFloat(),
                                whoPay
                            )
                        }

                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
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

                bill = if (it == "" || it == "0") "" else it

                if (toFloatorZero(bill) < toFloatorZero(expense)) {
                    expense = ""
                    friendExpense = ""
                    resultPay = ""
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_bill),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@FormSplitBill
                }

                if (bill == "" ||
                    expense == "" || expense == "0" ||
                    friendExpense == "" || friendExpense == "0"
                ) return@FormSplitBill

                resultPay = whoPay(context, whoPay, bill.toFloat(), expense.toFloat())

            },
            expense = expense,
            onExpenseChange = {
                expense = if (it == "" || it == "0") "" else it

                if (toFloatorZero(expense) > toFloatorZero(bill)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_expense),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@FormSplitBill
                }

                friendExpense =
                    if (expense == "") "" else (bill.toFloat() - it.toFloat()).toString()

                if (bill == "" || bill == "0" ||
                    expense == "" || expense == "0" ||
                    friendExpense == "" || friendExpense == "0"
                ) return@FormSplitBill

                resultPay = whoPay(context, whoPay, bill.toFloat(), expense.toFloat())
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

                resultPay = whoPay(context, whoPay, bill.toFloat(), expense.toFloat())
            },
            shareData = {
                shareData(
                    context,
                    whoPay(context, whoPay, bill.toFloat(), expense.toFloat())
                )
            },
            resetState = {
                name = ""
                bill = ""
                expense = ""
                friendExpense = ""
                whoPay = R.string.me
                resultPay = ""
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSplitBill(
    name: String, onNameChange: (String) -> Unit,
    bill: String, onBillChange: (String) -> Unit,
    expense: String, onExpenseChange: (String) -> Unit,
    friendExpense: String, resultPay: String,
    whoPay: Int, onPayChange: (Int) -> Unit,
    shareData: () -> Unit, resetState: () -> Unit,
    modifier: Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val payByDropdown = listOf(
        R.string.me,
        R.string.friend
    )

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
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
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
                    singleLine = true,
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
                    singleLine = true,
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
                    singleLine = true,
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
                        value = stringResource(id = whoPay),
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
                        payByDropdown.forEach { pay ->
                            DropdownMenuItem(
                                text = { Text(stringResource(id = pay)) },
                                onClick = {
                                    onPayChange(pay)
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
                        resetState()
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
                    shareData()
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

fun whoPay(context: Context, pay: Int, bill: Float, expense: Float): String {
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
            R.string.friend -> {
                context.getString(
                    R.string.result,
                    (bill).toString(),
                    (expense).toString(),
                    formatter.format(bill - expense),
                    "you",
                    "your friend",
                    formatter.format(bill - expense)
                )
            }

            R.string.me -> {
                context.getString(
                    R.string.result,
                    (bill).toString(),
                    (expense).toString(),
                    formatter.format(bill - expense),
                    "you",
                    "your friend",
                    formatter.format(bill - expense)
                )
            }

            else -> ""
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun DisplayAlertDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.pesan_hapus)) },
            confirmButton = {
                TextButton(onClick = { onConfirmation() }) {
                    Text(text = stringResource(R.string.tombol_hapus))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.tombol_batal))
                }
            },
            onDismissRequest = { onDismissRequest() }
        )
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

fun toFloatorZero(number: String): Float {
    return number.toFloatOrNull() ?: 0f
}


@Preview(showBackground = true)
@Composable
fun PrevDetail() {
    DetailScreen(rememberNavController())
}