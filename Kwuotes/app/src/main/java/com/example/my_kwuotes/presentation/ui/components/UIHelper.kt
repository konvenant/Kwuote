package com.example.my_kwuotes.presentation.ui.components

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kwuotes.R
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Red0
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddQuoteDialog(
    onDismissRequest: () -> Unit,
    onSave: (QuoteEntity) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userPrefManager = UserPrefManager(context)

    val user = userPrefManager.getImageFromPreferences(context)

    val currentDate = LocalDate.now()

    val formattedDate = currentDate.format( DateTimeFormatter.ofPattern("yyyy-MM-dd"))



    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = context.resources.getString(R.string.add_new_quote)) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item { Text(context.resources.getString(R.string.content))
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) }


                item {
                    Text(context.resources.getString(R.string.tags_comma_separated))
                    BasicTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    val newQuote = QuoteEntity(
                        id = 0,
                        quoteId = UUID.randomUUID().toString(),
                        author = user.name,
                        authorSlug = "${user.name}_slug",
                        content = content,
                        dateAdded = formattedDate,
                        dateModified = formattedDate,
                        length = content.length,
                        tags = tags.split(",").map { it.trim() },
                        category = "my_quotes"
                    )
                    onSave(newQuote)
                    onDismissRequest()
                },
                enabled = content.isNotEmpty() && tags.isNotEmpty()
            ) {
                Text(context.resources.getString(R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(context.resources.getString(R.string.cancel))
            }
        }
    )
}


@Composable
fun CustomToast(
    message: String,
    backgroundColor: Color = Red0,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = shape)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}


@SuppressLint("RememberReturnType")
@Composable
fun ShowCustomToast(message: String, backgroundColor: Color = Blue02) {
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message)
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {
            snackbarHostState.currentSnackbarData?.let { it1 ->
                Snackbar(
                    snackbarData = it1,
                    contentColor = Color.White, // Ensure text is visible against background
                    containerColor = backgroundColor
                )
            }
        }
    )
}

@Composable
fun LoadingDialog(
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    message: String = "Loading..."
) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismissRequest) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = message, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}


@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Confirm Deletion") },
        text = { Text(text = "Are you sure you want to delete this quote?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        }
    )
}

