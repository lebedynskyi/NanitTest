package com.example.birthday.presentation.birthday

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.birthday.R
import com.example.birthday.presentation.base.theme.LocalTheme

val IMAGE_NUMBERS = buildMap {
    put("0", R.drawable.ic_zero)
    put("1", R.drawable.ic_one)
    put("2", R.drawable.ic_two)
    put("3", R.drawable.ic_three)
    put("4", R.drawable.ic_four)
    put("5", R.drawable.ic_five)
    put("6", R.drawable.ic_six)
    put("7", R.drawable.ic_seven)
    put("8", R.drawable.ic_eight)
    put("9", R.drawable.ic_nine)
    put("10", R.drawable.ic_ten)
    put("11", R.drawable.ic_eleven)
    put("12", R.drawable.ic_twelve)
}


@Composable
fun BackBtn(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clickable {
                onClick()
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
fun CameraBtn(
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
fun BackgroundBox(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(LocalTheme.current.theme.bgImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun AvatarBox(
    childAvatarUri: Uri?,
    modifier: Modifier = Modifier,
) {
    val themeController = LocalTheme.current

    AsyncImage(
        model = childAvatarUri ?: themeController.theme.avatarPlaceHolder,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape = CircleShape)
    )
}

@Composable
fun NanitLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(R.drawable.ic_nanit),
        modifier = modifier,
        contentDescription = null
    )
}

@Composable
fun ShareNews(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.height(42.dp),
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
fun AgeBox(
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
        ) {
            Image(
                painter = painterResource(R.drawable.ic_left_swirls),
                contentDescription = null,
                modifier = Modifier.padding(end = 22.dp)
            )
            if (childAge > 12 && childAgeType == BirthdayType.YEAR) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    childAge.toString().forEach {
                        Image(
                            painterResource(IMAGE_NUMBERS.getValue(it.toString())),
                            contentDescription = null
                        )
                    }
                }
            } else {
                Image(
                    painterResource(IMAGE_NUMBERS.getValue(childAge.toString())),
                    contentDescription = null
                )
            }

            Image(
                painter = painterResource(R.drawable.ic_left_swirls),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 22.dp)
                    .rotate(180F)
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
