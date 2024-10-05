package uz.toshmatov.currency.presentation.main.screen.setting.feature.language.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yariksoffice.lingver.Lingver
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    languageModel: LanguageModel,
    onLanguageSelected: (String) -> Unit,
) {
    val currentLanguage = remember { Lingver.getInstance().getLocale() }
    val isSelected = remember { languageModel.code == currentLanguage.language }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CurrencyColors.bottomBar)
            .selectable(
                selected = isSelected,
                onClick = { onLanguageSelected(languageModel.code) },
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(32.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(languageModel.icon).crossfade(true).build(),
            contentDescription = "This is an example image",
            placeholder = painterResource(id = drawable.ic_empty_flag)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 40.dp),
            text = languageModel.title.resource,
            color = CurrencyColors.text,
            style = CurrencyTypography.textMedium
        )
        if (isSelected) {
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd),
                painter = painterResource(id = drawable.ic_active),
                contentDescription = languageModel.title.resource,
                tint = CurrencyColors.icon
            )
        }
    }
}