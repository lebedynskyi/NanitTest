package com.example.birthday.presentation.birthday

import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.theme.AppTheme
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme

private val IMAGE_NUMBERS = buildMap {
    put('0', R.drawable.ic_zero)
    put('1', R.drawable.ic_one)
    put('2', R.drawable.ic_two)
    put('3', R.drawable.ic_three)
    put('4', R.drawable.ic_four)
    put('5', R.drawable.ic_five)
    put('6', R.drawable.ic_six)
    put('7', R.drawable.ic_seven)
    put('8', R.drawable.ic_eight)
    put('9', R.drawable.ic_nine)
}

@Composable
fun BirthdayPage(
    viewState: BirthdayViewState
) {
    BirthdayPageContent(
        viewState.childName,
        viewState.childAge,
        viewState.childAgeType,
        viewState.childAvatarUri
    )
}

@Composable
private fun BirthdayPageContent(
    childName: String?,
    childAge: Int?,
    childAgeType: BirthdayType?,
    childAvatarUri: Uri?
) {
    var footerHeight by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        ChildAvatar(
            childAvatarUri,
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

        if (childName != null && childAge != null && childAgeType != null) {
            ChildAge(
                childName = childName,
                childAge = childAge,
                childAgeType = childAgeType,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            )
        }

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
private fun ChildAge(
    childName: String,
    childAge: Int,
    childAgeType: BirthdayType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            stringResource(R.string.today_is, childName),
            textAlign = TextAlign.Center,
            fontSize = 21.sp,
            color = Color(0xFF394562),
            modifier = Modifier.fillMaxWidth(0.6F)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Image(painterResource(R.drawable.ic_left_swirls), contentDescription = null)
            childAge.toString().forEach {
                Image(
                    painterResource(IMAGE_NUMBERS.getValue(it)),
                    contentDescription = null
                )
            }
            Image(
                painterResource(R.drawable.ic_left_swirls),
                modifier = Modifier.rotate(180F),
                contentDescription = null
            )
        }

        Text(
            if (childAgeType == BirthdayType.MONTH) {
                pluralStringResource(R.plurals.old_month, childAge)
            } else {
                pluralStringResource(R.plurals.old_years, childAge)
            },
            fontSize = 21.sp,
            color = Color(0xFF394562)
        )
    }
}


@Composable
private fun ChildAvatar(
    childAvatarUri: Uri?,
    modifier: Modifier = Modifier
) {
    val themeController = LocalTheme.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val bottomMargin = if (screenHeight > 700) 52.dp else 0.dp
    val scaleFactor = if (screenHeight > 700) 0.8F else 0.65F
    System.err.println("Height DP $screenHeight")
    System.err.println("Scale DP $scaleFactor")

    AsyncImage(
        model = childAvatarUri ?: themeController.theme.avatarImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(bottom = bottomMargin)
            .fillMaxWidth(scaleFactor)
            .aspectRatio(1f)
            .clip(shape = CircleShape)
            .clickable {
                themeController.switchTo((AppTheme.entries - themeController.theme).random())
            },
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
            Text(
                stringResource(R.string.share_the_news),
                color = Color.White
            )
            Image(
                painterResource(R.drawable.ic_share),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
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
