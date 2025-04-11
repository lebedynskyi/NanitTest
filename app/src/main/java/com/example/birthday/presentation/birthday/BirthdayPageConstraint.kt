package com.example.birthday.presentation.birthday

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.theme.AppTheme
import com.example.birthday.presentation.base.theme.BirthdayTheme
import com.example.birthday.presentation.base.theme.LocalTheme
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun BirthdayPageConstraint(
    viewState: BirthdayViewState,
    onEvent: (UiEvent) -> Unit = {},
    onBackNavigation: () -> Unit = {},
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { onEvent(BirthdayPageUiEvent.OnAvatarChanged(it)) }
    }

    val themeController = LocalTheme.current

    BirthdayPageConstraintContent(
        viewState = viewState,
        onBackClick = onBackNavigation,
        onAvatarCameraClick = { launcher.launch("image/*") },
        onShareScreenShot = { onEvent(BirthdayPageUiEvent.OnShareScreenShot(it)) },
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
fun BirthdayPageConstraintContent(
    viewState: BirthdayViewState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAvatarCameraClick: () -> Unit = {},
    onShareScreenShot: (Bitmap) -> Unit = {}
) {
    val graphicsLayer = rememberGraphicsLayer()
    var captureImage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var avatarLayout: LayoutCoordinates? by remember { mutableStateOf(null) }
    var cameraLayout: LayoutCoordinates? by remember { mutableStateOf(null) }

    val configuration = LocalConfiguration.current

    // TODO Fore review: In design it is not clear where to get extra space space for tall screen.
    // Now the psace between NANIT logog and Share button is used. Looks good
    val tallScreen = configuration.screenHeightDp > 700
    val tallScreenOffsetY = if (tallScreen) 60.dp else 0.dp

    LaunchedEffect(captureImage) {
        if (captureImage) {
            coroutineScope.launch {
                val bitmap = graphicsLayer.toImageBitmap()
                onShareScreenShot(bitmap.asAndroidBitmap())
            }
        }
        captureImage = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
                .background(MaterialTheme.colorScheme.background)
        ) {
            val (avaBox, logo, age) = createRefs()

            AvatarBox(
                viewState.childAvatarUri, modifier = Modifier
                    .constrainAs(avaBox) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(logo.top, margin = 15.dp)
                    }
                    .padding(horizontal = 64.dp)
                    .aspectRatio(1f)
                    .onGloballyPositioned {
                        avatarLayout = it
                    }
            )

            BackgroundBox(modifier = Modifier.fillMaxSize())
            NanitLogo(modifier.constrainAs(logo) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 148.dp + tallScreenOffsetY)
            })

            AgeBox(
                viewState.childName.orEmpty(),
                viewState.childAge ?: 0,
                viewState.childAgeType ?: BirthdayType.MONTH,
                modifier = Modifier.constrainAs(age) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(avaBox.top)
                }
            )
        }

        // Non drawable content
        ShareNews(
            onClick = { captureImage = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 53.dp)
        )

        CameraBtn(
            onClick = onAvatarCameraClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset {
                    val avatarTop = avatarLayout?.positionInParent()?.y?.toInt() ?: 0
                    val cameraHalfSize = (cameraLayout?.size?.height ?: 0) / 2
                    val yOffset = avatarTop - cameraHalfSize

                    val angleRadians = Math.toRadians(45.0)
                    val radius = (avatarLayout?.size?.height ?: 0) / 2
                    val radiusXOffset = (radius * cos(angleRadians)).toInt()
                    val radiusYOffset = (radius * sin(angleRadians)).toInt() - radius

                    IntOffset(radiusXOffset, yOffset - radiusYOffset)
                }
                .onGloballyPositioned {
                    cameraLayout = it
                }
        )

        BackBtn(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        )
    }
}

private fun requestShareFile(
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
        BirthdayPageConstraint(
            BirthdayViewState(
                childName = "Cristiano",
                childAge = 12,
                childAgeType = BirthdayType.YEAR
            )
        )
    }
}