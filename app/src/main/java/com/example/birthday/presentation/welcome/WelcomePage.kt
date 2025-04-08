@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.birthday.presentation.welcome

import android.net.Uri
import androidx.compose.ui.platform.LocalFocusManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme
import com.example.birthday.presentation.base.theme.Typography
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun WelcomePage(
    viewState: WelcomeViewState, onEvent: (UiEvent) -> Unit = {}
) {
    val mediaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { onEvent(WelcomeUIEvent.OnAvatarSelected(it)) }
    }

    Box(contentAlignment = Alignment.TopCenter) {
        WelcomePageTopBar(Modifier.padding(top = 64.dp))
        WelcomePageContent(
            name = viewState.childName,
            date = viewState.childBirthday,
            imageUri = viewState.childAvatarUri,
            showBtnEnabled = viewState.showBtnEnabled,
            onNameChanged = { onEvent(WelcomeUIEvent.NameChanged(it)) },
            onDateChanged = { onEvent(WelcomeUIEvent.DateChanged(it)) },
            onShowPressed = { onEvent(WelcomeUIEvent.OnShowBirthday) },
            onAvatarClicked = { mediaLauncher.launch("image/*") },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun WelcomePageTopBar(modifier: Modifier) {
    Text(
        stringResource(R.string.app_name),
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = Typography.titleLarge
    )
}

@Composable
private fun WelcomePageContent(
    name: String?,
    date: LocalDateTime?,
    imageUri: Uri?,
    showBtnEnabled: Boolean,
    onNameChanged: (String) -> Unit,
    onDateChanged: (Long) -> Unit,
    onAvatarClicked: () -> Unit,
    onShowPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val themeController = LocalTheme.current
    val focusManager = LocalFocusManager.current

    if (showDatePicker) {
        BirthdayDatePicker(
            date,
            onDismiss = { showDatePicker = false },
            onConfirm = {
                showDatePicker = false
                onDateChanged(it)
            })
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUri,
            placeholder = painterResource(themeController.theme.avatarImage),
            fallback = painterResource(themeController.theme.avatarImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onError = {
                System.err.println("Unable to display $it")
            },
            modifier = Modifier
                .fillMaxWidth(0.6F)
                .aspectRatio(1F)
                .clip(CircleShape)
                .clickable {
                    onAvatarClicked()
                }
        )

        OutlinedTextField(
            value = name.orEmpty(),
            label = { Text(stringResource(R.string.name)) },
            onValueChange = onNameChanged
        )

        OutlinedTextField(
            value = date?.toString().orEmpty(),
            label = { Text(stringResource(R.string.birthday)) },
            onValueChange = {},
            modifier = Modifier
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        showDatePicker = true
                    }
                    focusManager.clearFocus(true)
                }
        )

        Spacer(Modifier.size(12.dp))

        OutlinedButton(
            enabled = showBtnEnabled,
            onClick = onShowPressed
        ) {
            Text(
                stringResource(R.string.show_birthday_screen),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun BirthdayDatePicker(
    date: LocalDateTime?,
    onDismiss: () -> Unit = {},
    onConfirm: (Long) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState(date?.toInstant(ZoneOffset.UTC)?.toEpochMilli())
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let(onConfirm)
                }) {
                Text("OK")
            }
        },
        dismissButton = {
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