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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.Typography

@Composable
fun WelcomePage(
    viewState: WelcomeViewState,
    onEvent: (UiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = { WelcomePageTopBar() }
    ) { padding ->
        WelcomePageContent(
            name = viewState.childName,
            onNameChanged = { onEvent(WelcomeUIEvent.NameChanged(it)) },
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
        }
    )
}

@Composable
private fun WelcomePageContent(
    name: String?,
    onNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(OutlinedTextFieldDefaults.MinHeight),
        ) {
            Text(
                text = stringResource(R.string.birthday),
                style = LocalTextStyle.current,
                color = OutlinedTextFieldDefaults.colors().focusedTextColor,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.size(24.dp))

        OutlinedButton(
            onClick = {}
        ) {
            Text(stringResource(R.string.show_birthday_screen))
        }
    }
}

@Preview
@Composable
private fun WelcomePagePreview() {
    BirthdayTheme {
        WelcomePage(WelcomeViewState(""))
    }
}