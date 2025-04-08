package com.example.birthday.presentation.birthday

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
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
fun BirthdayConstraint(
    viewState: BirthdayViewState,
    onEvent: (UiEvent) -> Unit = {},
    onBackNavigation: () -> Unit = {},
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { onEvent(BirthdayPageUiEvent.OnAvatarChanged(it)) }
    }

    val themeController = LocalTheme.current

    LaunchedEffect(Unit) {
        themeController.switchTo((AppTheme.entries - themeController.theme).random())
    }

    BirthdayConstraintContent(
        viewState = viewState,
        onBackClick = onBackNavigation,
        onAvatarCameraClick = { launcher.launch("image/*") }
    )
}

@Composable
private fun BirthdayConstraintContent(
    viewState: BirthdayViewState,
    uiIsVisible: Boolean = true,
    onBackClick: () -> Unit,
    onAvatarCameraClick: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (avatarBox, ageBox, backBtn, cameraBtn, uiFooterBox) = createRefs()

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val avatarSize = 280.dp

        AvatarBox(
            childAvatarUri = viewState.childAvatarUri,
            modifier = Modifier
                .width(avatarSize)
                .height(avatarSize)
                .constrainAs(avatarBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(uiFooterBox.top, if (screenHeight > 700) 60.dp else 0.dp)
                }
        )

        BackgroundBox(Modifier.fillMaxSize())

        AgeBox(
            childName = viewState.childName.orEmpty(),
            childAge = viewState.childAge ?: 0,
            childAgeType = viewState.childAgeType ?: BirthdayType.MONTH,
            modifier = Modifier
                .constrainAs(ageBox) {
                    top.linkTo(parent.top, if (screenHeight > 700) 32.dp else 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        BackBtn(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(backBtn) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                visibility = if (uiIsVisible) Visibility.Visible else Visibility.Invisible
            }
        )

        CameraBtn(
            onClick = onAvatarCameraClick,
            modifier = Modifier.constrainAs(cameraBtn) {
                top.linkTo(avatarBox.top, margin = 24.dp)
                end.linkTo(avatarBox.end, margin = 24.dp)
                visibility = if (uiIsVisible) Visibility.Visible else Visibility.Invisible
            }
        )

        UIFooterBox(
            modifier = Modifier
                .constrainAs(uiFooterBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    visibility = if (uiIsVisible) Visibility.Visible else Visibility.Invisible
                }
        )
    }
}

@Composable
private fun BackBtn(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(R.drawable.ic_back),
        contentDescription = null,
        modifier = modifier.clickable {
            onClick()
        }
    )
}

@Composable
private fun CameraBtn(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val themeController = LocalTheme.current

    Image(
        painter = painterResource(themeController.theme.cameraImage),
        contentDescription = null,
        modifier = modifier.clickable {
            onClick()
        }
    )
}

@Composable
private fun BackgroundBox(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(LocalTheme.current.theme.bgImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
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
            .clip(shape = CircleShape)
    )
}

@Composable
private fun UIFooterBox(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painterResource(R.drawable.ic_nanit),
            modifier = Modifier.padding(top = 15.dp),
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

@Preview
@Composable
private fun PreviewBirthdayV2() {
    BirthdayTheme {
        BirthdayConstraint(
            BirthdayViewState(
                childName = "Cristiano",
                childAge = 12,
                childAgeType = BirthdayType.YEAR
            )
        )
    }
}
