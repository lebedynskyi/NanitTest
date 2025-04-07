@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.birthday.presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.Typography
import java.time.LocalDateTime

@Composable
fun WelcomePage(
    viewState: WelcomeViewState, onEvent: (UiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = { WelcomePageTopBar() }) { padding ->
        WelcomePageContent(
            name = viewState.childName,
            date = viewState.birthdayDate,
            onNameChanged = { onEvent(WelcomeUIEvent.NameChanged(it)) },
            onDateChanged = { onEvent(WelcomeUIEvent.DateChanged(it)) },
            onShowPressed = {onEvent(WelcomeUIEvent.OnShowBirthday)},
            modifier = Modifier
                .padding(padding.calculateTopPadding())
                .fillMaxSize()
        )
    }
}

@Composable
private fun WelcomePageTopBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                style = Typography.titleLarge,
            )
        })
}

@Composable
private fun WelcomePageContent(
    name: String?,
    date: LocalDateTime?,
    onNameChanged: (String) -> Unit,
    onDateChanged: (Long) -> Unit,
    onShowPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        BirthdayDatePicker(
            onDismiss = { showDatePicker = false },
            onConfirm = {
                showDatePicker = false
                onDateChanged(it)
            })
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = name.orEmpty(),
            label = { Text(stringResource(R.string.name)) },
            onValueChange = onNameChanged
        )

        Spacer(Modifier.size(12.dp))

        OutlinedButton(
            contentPadding = OutlinedTextFieldDefaults.contentPadding(),
            shape = OutlinedTextFieldDefaults.shape,
            onClick = { showDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(OutlinedTextFieldDefaults.MinHeight),
        ) {
            Text(
                text = date?.let { it.toString() } ?: stringResource(R.string.birthday),
                style = LocalTextStyle.current,
                color = OutlinedTextFieldDefaults.colors().focusedTextColor,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.size(24.dp))

        OutlinedButton(
            onClick = onShowPressed
        ) {
            Text(stringResource(R.string.show_birthday_screen))
        }
    }
}

@Composable
private fun BirthdayDatePicker(
    onDismiss: () -> Unit = {},
    onConfirm: (Long) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(
            onClick = {
                datePickerState.selectedDateMillis?.let(onConfirm)
            }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
private fun WelcomePagePreview() {
    BirthdayTheme {
        WelcomePage(WelcomeViewState())
    }
}