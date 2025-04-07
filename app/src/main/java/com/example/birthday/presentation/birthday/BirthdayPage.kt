package com.example.birthday.presentation.birthday

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birthday.R
import com.example.birthday.presentation.base.theme.AppTheme
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme

@Composable
fun BirthdayPage(
    viewState: BirthdayViewState
) {
    BirthdayPageContent(viewState.childName)
}

@Composable
private fun BirthdayPageContent(childName: String?) {
    var footerHeight by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        ChildAge(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
                .fillMaxWidth(0.6F)
        )

        ChildAvatar(
            Modifier
                .align(Alignment.BottomCenter)
                .offset {
                    IntOffset(0, -footerHeight)
                })

        Image(
            painterResource(LocalTheme.current.theme.bgImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        ChildFooter(
            Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    footerHeight = it.size.height
                }
        )
    }
}

@Composable
private fun ChildAge(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "TODAY CRISTIANO RONALDO IS",
            textAlign = TextAlign.Center,
            fontSize = 21.sp,
            color = Color(0xFF394562)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Image(painterResource(R.drawable.ic_left_swirls), contentDescription = null)
            Image(painterResource(R.drawable.ic_age_1), contentDescription = null)
            Image(
                painterResource(R.drawable.ic_left_swirls),
                modifier = Modifier.rotate(180F),
                contentDescription = null
            )
        }

        Text(
            "MONTH OLD!",
            fontSize = 21.sp,
            color = Color(0xFF394562)
        )
    }
}


@Composable
private fun ChildAvatar(modifier: Modifier = Modifier) {
    val themeController = LocalTheme.current
    Image(
        painterResource(themeController.theme.avatarImage),
        modifier = modifier
            .fillMaxWidth(0.65F)
            .aspectRatio(1f)
            .clickable {
                themeController.switchTo((AppTheme.entries - themeController.theme).random())
            },
        contentDescription = null,
    )
}

@Composable
private fun ChildFooter(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painterResource(R.drawable.ic_nanit),
            modifier = Modifier.padding(top = 20.dp),
            contentDescription = null
        )

        Button(
            modifier = Modifier
                .padding(vertical = 53.dp)
                .height(42.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF7B7B))
        ) {
            Text("Share the news")
            Image(
                painterResource(R.drawable.ic_share),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBirthdayPage() {
    BirthdayTheme {
        BirthdayPage(BirthdayViewState("Cristiano"))
    }
}
