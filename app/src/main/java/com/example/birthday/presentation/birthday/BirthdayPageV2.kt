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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme
import kotlin.math.cos
import kotlin.math.sin

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
fun BirthdayV2(
    viewState: BirthdayViewState,
    onEvent: (UiEvent) -> Unit = {},
) {
    BirthdayV2Content(viewState)
}

@Composable
private fun BirthdayV2Content(
    viewState: BirthdayViewState,
    uiIsVisible: Boolean = true
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var avatarPosition by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val bottomMargin = if (screenHeight > 700) 52.dp else 0.dp
    val scaleFactor = if (screenHeight > 700) 0.8F else 0.65F

    Box(Modifier.fillMaxSize()) {
        AvatarBox(
            childAvatarUri = viewState.childAvatarUri,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomMargin)
                .onGloballyPositioned {
                    avatarPosition = it
                }
        )

        BackgroundBox()

        if (viewState.ageVisible()) {
            AgeBox(
                childName = viewState.childName.orEmpty(),
                childAge = viewState.childAge ?: 0,
                childAgeType = viewState.childAgeType ?: BirthdayType.MONTH,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
        }

        if (uiIsVisible) {
            UiBox(avatarPosition = avatarPosition)
        }
    }
}

@Composable
private fun AvatarBox(
    childAvatarUri: Uri?,
    modifier: Modifier = Modifier,
) {
    val themeController = LocalTheme.current

    AsyncImage(
        model = childAvatarUri ?: themeController.theme.avatarPlaceHolder,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape = CircleShape)
    )
}

@Composable
private fun BackgroundBox() {
    Image(
        painter = painterResource(LocalTheme.current.theme.bgImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun AgeBox(
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
            stringResource(R.string.today_is, childName).uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 21.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
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
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun UiBox(
    avatarPosition: LayoutCoordinates?,
    onAvatarCameraClick: () -> Unit = {},
) {
    val themeController = LocalTheme.current
    val angleRadians = Math.toRadians(315.0)
    var cameraImagePosition by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(themeController.theme.cameraImage),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable {
                    onAvatarCameraClick()
                }
                .onGloballyPositioned {
                    cameraImagePosition = it
                }
                .offset {
                    val cameraData = cameraImagePosition
                    if (avatarPosition != null && cameraData != null) {
                        val selfPositionOffset = IntOffset(0, avatarPosition.size.height / 2)
                        System.err.println("Self offset = $selfPositionOffset")
                        val avatarPositionOffset = IntOffset(
                            avatarPosition.positionInParent().x.toInt(),
                            avatarPosition.positionInParent().y.toInt()
                        )
                        System.err.println("Avatar offset = $avatarPositionOffset")
                        val radiusOffset = IntOffset(
                            (((avatarPosition.size.width / 2) - (cameraData.size.width / 2)) * cos(angleRadians)).toInt(),
                            (((avatarPosition.size.height / 2) - (cameraData.size.height / 2)) * sin(angleRadians)).toInt()
                        )
                        val offset = radiusOffset - selfPositionOffset + avatarPositionOffset
                        System.err.println("Offset $offset")
                        offset
                    } else {
                        IntOffset(0, 0)
                    }
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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
}


@Preview
@Composable
private fun PreviewBirthdayV2() {
    BirthdayTheme {
        BirthdayV2(
            BirthdayViewState(
                childName = "Cristiano",
                childAge = 12,
                childAgeType = BirthdayType.YEAR
            )
        )
    }
}
