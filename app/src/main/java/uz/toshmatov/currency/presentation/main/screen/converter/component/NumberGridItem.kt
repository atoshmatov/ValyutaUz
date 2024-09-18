package uz.toshmatov.currency.presentation.main.screen.converter.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NumberGridItem(
    modifier: Modifier = Modifier,
    numberClick: (String) -> Unit
) {
    val numbers = remember {
        listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            ",", "0", "C"
        )
    }

    val number = remember {
        mutableStateListOf<String>()
    }

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(CurrencyDimensions.extraSmall),
        verticalItemSpacing = CurrencyDimensions.extraSmall
    ) {
        items(12) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .clip(RoundedCornerShape(CurrencyDimensions.small))
                    .background(CurrencyColors.bottomBar)
                    .clickable {
                        if (numbers[it] == "C") {
                            if (number.isNotEmpty())
                                number.removeAt(number.size - 1)
                        } else if (numbers[it] == ",") {
                            if (number.isNotEmpty())
                                number.add(".")
                        } else if (numbers[it] == "0" && number.isEmpty()) {
                            number.add("0.")
                        } else {
                            number.add(numbers[it])
                        }
                        number
                            .toList()
                            .joinToString("")
                            .let { number ->
                                numberClick(number)
                            }
                    }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = numbers[it],
                    color = CurrencyColors.text,
                    style = CurrencyTypography.buttonCalculator
                )
            }
        }
    }
}