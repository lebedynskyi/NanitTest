package com.example.birthday.presentation.birthday

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.AppTheme
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme
import java.io.File
import kotlinx.coroutines.launch

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

    BirthdayConstraintContent(
        viewState = viewState,
        onBackClick = onBackNavigation,
        onAvatarCameraClick = { launcher.launch("image/*") },
        onShareScreenShot = { onEvent(BirthdayPageUiEvent.OnShareScreenShot(it)) },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )

    LaunchedEffect(Unit) {
        themeController.switchTo((AppTheme.entries - themeController.theme).random())
    }

    viewState.shareUri?.let {
        requestShareFile(LocalActivity.current!!, it.path.orEmpty())
        viewState.shareUri = null
    }
}

@Composable
private fun BirthdayConstraintContent(
    viewState: BirthdayViewState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAvatarCameraClick: () -> Unit = {},
    onShareScreenShot: (Bitmap) -> Unit = {}
) {
    var footerLayout: LayoutCoordinates? by remember { mutableStateOf(null) }
    var avatarLayout: LayoutCoordinates? by remember { mutableStateOf(null) }
    var cameraLayout: LayoutCoordinates? by remember { mutableStateOf(null) }
    val graphicsLayer = rememberGraphicsLayer()
    var captureImage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val tallScreen = configuration.screenHeightDp > 700

    LaunchedEffect(captureImage) {
        if (captureImage) {
            coroutineScope.launch {
                val bitmap = graphicsLayer.toImageBitmap()
                onShareScreenShot(bitmap.asAndroidBitmap())
            }
        }
        captureImage = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }) {
        val avatarSize = 280.dp
        val density = LocalDensity.current

        AvatarBox(
            childAvatarUri = viewState.childAvatarUri,
            modifier = Modifier
                .width(avatarSize)
                .height(avatarSize)
                .align(Alignment.BottomCenter)
                .offset {
                    val tallScreenOffsetY = if (tallScreen) with(density) { 60.dp.toPx() } else 0F
                    val footerHeight = footerLayout?.size?.height ?: 0
                    IntOffset(0, -footerHeight - tallScreenOffsetY.toInt())
                }
                .onGloballyPositioned { avatarLayout = it }
        )

        BackgroundBox(Modifier.fillMaxSize())

        AgeBox(
            childName = viewState.childName.orEmpty(),
            childAge = viewState.childAge ?: 0,
            childAgeType = viewState.childAgeType ?: BirthdayType.MONTH,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (tallScreen) 32.dp else 20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackBtn(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        )

        CameraBtn(
            onClick = onAvatarCameraClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset {
                    // TODO this is not done. Trying to place icon on 45 degree angle.
                    //  Need calculate cos and sin according center of the avatar
                    val tallScreenOffsetY = if (tallScreen) with(density) { 60.dp.toPx() } else 0F
                    val footerHeight = footerLayout?.size?.height ?: 0
                    val avatarHeight = avatarLayout?.size?.height ?: 0
                    val cameraSizeOffset = (cameraLayout?.size?.height ?: 0) / 2
                    IntOffset(0, -footerHeight - avatarHeight - tallScreenOffsetY.toInt() + cameraSizeOffset)
                }.onGloballyPositioned {
                    cameraLayout = it
                }
        )

        FooterBox(
            onClick = { captureImage = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    footerLayout = it
                }
        )
    }
}

@Composable
private fun BackBtn(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clickable {
                onClick
            }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
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
private fun FooterBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            onClick = { onClick() },
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

fun requestShareFile(
    activity: Activity,
    filePath: String,
) {
    val uri =
        FileProvider.getUriForFile(activity, activity.packageName + ".provider", File(filePath))
    ShareCompat.IntentBuilder(activity)
        .setChooserTitle("Choose application to share a ")
        .setType("*/*")
        .addStream(uri)
        .startChooser()
}

@Preview
@Composable
private fun PreviewBirthdayConstraint() {
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
