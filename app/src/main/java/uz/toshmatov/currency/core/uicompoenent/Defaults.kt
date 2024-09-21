@file:OptIn(ExperimentalMaterial3Api::class)

package uz.toshmatov.currency.core.uicompoenent

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = titleId.resource,
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.text,
            )
        },
        modifier = modifier.shadow(16.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = CurrencyColors.text),
    )
}

@Composable
fun TopBar(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    color: Color = Color.Transparent,
    contentDescription: String,
) {
    TopAppBar(
        modifier = modifier
            .padding(start = CurrencyDimensions.medium),
        title = {
            Text(
                text = titleId.resource,
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.text,
                modifier = Modifier.padding(start = CurrencyDimensions.small),
            )
        },
        navigationIcon = {
            CurrencyIcon(
                image = drawable.ic_arrow_left,
                tint = CurrencyColors.text,
                onClick = onBackClick,
                contentDescription = contentDescription,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(color),
    )
}

@Composable
fun AvatarLoader(
    url: String,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    placeholderRes: Int = drawable.ic_tab_setting,
    color: Color = CurrencyColors.text,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        placeholder = painterResource(placeholderRes),
        error = painterResource(placeholderRes),
        contentDescription = null,
        alignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onAvatarClick() }
            .fillMaxSize()
            .background(color = color),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun AvatarLoader(
    uri: Uri,
    modifier: Modifier = Modifier,
    placeholderRes: Int = drawable.ic_tab_setting,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(uri).crossfade(true).build(),
        placeholder = painterResource(placeholderRes),
        error = painterResource(placeholderRes),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(color = CurrencyColors.background),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun CommetaDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = CurrencyColors.button
    )
}
