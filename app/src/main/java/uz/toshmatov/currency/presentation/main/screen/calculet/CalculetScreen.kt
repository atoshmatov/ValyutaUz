package uz.toshmatov.currency.presentation.main.screen.calculet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

object CalculetScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon =
                rememberVectorPainter(ImageVector.vectorResource(id = drawable.ic_tab_repeat))
            val title = string.tab_calculet.resource

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        CalculetScreenContent()
    }
}

@Composable
fun CalculetScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = remember { mutableStateOf("") }
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            placeholder = {
                Text(text = "Assalomu")
            }
        )
    }
}
