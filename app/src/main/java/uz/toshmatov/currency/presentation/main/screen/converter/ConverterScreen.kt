package uz.toshmatov.currency.presentation.main.screen.converter

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import uz.toshmatov.currency.core.extensions.convertSomToDouble
import uz.toshmatov.currency.core.extensions.formatNumberDynamically
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyIconButton
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.presentation.main.screen.converter.component.ConverterItem
import uz.toshmatov.currency.presentation.main.screen.converter.component.NumberGridItem
import uz.toshmatov.currency.presentation.main.screen.converter.component.NumberKeyboardAction

class ConverterScreen(
    val codeName: String,
    val code: String,
    val rate: String
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<ConverterViewModel>()
        val availableCurrencies by viewModel.currencies.collectAsStateWithLifecycle()

        ConverterScreenContent(
            backClick = navigator::pop,
            codeName = codeName,
            code = code,
            rate = rate,
            availableCurrencies = availableCurrencies
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ConverterScreenContent(
    backClick: () -> Unit,
    codeName: String,
    code: String = "",
    rate: String = "",
    availableCurrencies: List<CBUModel> = emptyList(),
) {
    var enteredAmount by remember { mutableStateOf(DEFAULT_INPUT) }
    var cursorIndex by remember { mutableIntStateOf(DEFAULT_INPUT.length) }
    var isSelectedCurrencyToSom by remember { mutableStateOf(true) }
    var isSwapAnimating by remember { mutableStateOf(false) }
    var showCurrencyPicker by remember { mutableStateOf(false) }
    var selectedCurrency by remember(codeName, code, rate) {
        mutableStateOf(
            ConverterCurrency(
                codeName = codeName,
                code = code,
                rate = rate
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val swapProgress = remember { Animatable(0f) }

    val activeCurrency by remember(selectedCurrency, availableCurrencies) {
        derivedStateOf {
            availableCurrencies
                .firstOrNull { it.ccy.equals(selectedCurrency.code, ignoreCase = true) }
                ?.toConverterCurrency()
                ?: selectedCurrency
        }
    }

    val currencyOptions by remember(availableCurrencies, activeCurrency) {
        derivedStateOf {
            (availableCurrencies.map { it.toConverterCurrency() } + activeCurrency)
                .filter { it.code.isNotBlank() && it.rate.isNotBlank() }
                .filterNot { it.code.equals(SOM_CURRENCY_CODE, ignoreCase = true) }
                .filterNot { it.code.equals(SOM_LABEL, ignoreCase = true) }
                .distinctBy { it.code.uppercase() }
                .sortedBy { it.code.uppercase() }
        }
    }

    val rateValue = remember(activeCurrency.rate) { activeCurrency.rate.convertSomToDouble() }
    val switchRotation by animateFloatAsState(
        targetValue = when {
            isSwapAnimating && isSelectedCurrencyToSom -> 180f
            isSwapAnimating && !isSelectedCurrencyToSom -> 0f
            isSelectedCurrencyToSom -> 0f
            else -> 180f
        },
        animationSpec = tween(
            durationMillis = 280,
            easing = FastOutSlowInEasing
        ),
        label = "converter_switch_rotation"
    )

    val sourceCode by remember(isSelectedCurrencyToSom, activeCurrency.code) {
        derivedStateOf { if (isSelectedCurrencyToSom) activeCurrency.code else SOM_LABEL }
    }
    val sourceCodeName by remember(isSelectedCurrencyToSom, activeCurrency.codeName) {
        derivedStateOf { if (isSelectedCurrencyToSom) activeCurrency.codeName else SOM_LABEL }
    }
    val sourceRateText by remember(isSelectedCurrencyToSom, rateValue, activeCurrency.code) {
        derivedStateOf {
            if (isSelectedCurrencyToSom) {
                formatAmountWithCode(rateValue, SOM_LABEL)
            } else {
                formatAmountWithCode(
                    value = rateValue.inverseRate(),
                    code = activeCurrency.code
                )
            }
        }
    }

    val targetCode by remember(isSelectedCurrencyToSom, activeCurrency.code) {
        derivedStateOf { if (isSelectedCurrencyToSom) SOM_LABEL else activeCurrency.code }
    }
    val targetCodeName by remember(isSelectedCurrencyToSom, activeCurrency.codeName) {
        derivedStateOf { if (isSelectedCurrencyToSom) SOM_LABEL else activeCurrency.codeName }
    }
    val targetRateText by remember(isSelectedCurrencyToSom, rateValue, activeCurrency.code) {
        derivedStateOf {
            if (isSelectedCurrencyToSom) {
                formatAmountWithCode(
                    value = rateValue.inverseRate(),
                    code = activeCurrency.code
                )
            } else {
                formatAmountWithCode(rateValue, SOM_LABEL)
            }
        }
    }

    val sourceFormattedAmount by remember(enteredAmount) {
        derivedStateOf { formatEditableNumber(enteredAmount) }
    }
    val sourceEditableAmount by remember(sourceFormattedAmount, cursorIndex) {
        derivedStateOf {
            withCursorMarker(
                formattedAmount = sourceFormattedAmount,
                rawCursorIndex = cursorIndex
            )
        }
    }
    val sourceAmountText by remember(sourceEditableAmount, sourceCode) {
        derivedStateOf { "$sourceEditableAmount $sourceCode" }
    }
    val convertedAmount by remember(enteredAmount, isSelectedCurrencyToSom, rateValue, targetCode) {
        derivedStateOf {
            val baseAmount = enteredAmount.convertSomToDouble()
            val converted = if (isSelectedCurrencyToSom) {
                baseAmount * rateValue
            } else {
                if (rateValue > 0.0) baseAmount / rateValue else 0.0
            }
            formatAmountWithCode(value = converted, code = targetCode)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
    ) {
        val density = LocalDensity.current
        val isCompactScreen = maxHeight < SMALL_SCREEN_THRESHOLD_DP
        val keyHeight = if (isCompactScreen) SMALL_KEY_HEIGHT else DEFAULT_KEY_HEIGHT
        val spacing = if (isCompactScreen) CurrencyDimensions.extraSmall else CurrencyDimensions.small
        val switchSize = SWITCH_BUTTON_SIZE
        val spacingPx = with(density) { spacing.roundToPx() }
        val switchSizePx = with(density) { switchSize.roundToPx() }
        val minCardHeightPx = with(density) { MIN_CARD_HEIGHT_DP.roundToPx() }

        var topCardHeightPx by remember { mutableIntStateOf(minCardHeightPx) }
        var bottomCardHeightPx by remember { mutableIntStateOf(minCardHeightPx) }

        val maxCardHeightPx = maxOf(topCardHeightPx, bottomCardHeightPx, minCardHeightPx)
        val cardTravelPx = maxCardHeightPx + spacingPx + switchSizePx + spacingPx
        val cardsContainerHeightPx = cardTravelPx + maxCardHeightPx
        val cardsContainerHeight = with(density) { cardsContainerHeightPx.toDp() }
        val currentSwapProgress = swapProgress.value
        val topCardTranslationY = cardTravelPx * currentSwapProgress
        val bottomCardTranslationY = cardTravelPx * (1f - currentSwapProgress)
        val topCardZ = if (currentSwapProgress <= 0.5f) 2f else 1f
        val bottomCardZ = if (currentSwapProgress <= 0.5f) 1f else 2f

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopBar(
                titleId = string.tab_converter,
                onBackClick = backClick,
                contentDescription = "back"
            )
            Box(
                modifier = Modifier
                    .height(cardsContainerHeight)
                    .padding(horizontal = CurrencyDimensions.medium)
            ) {
                ConverterItem(
                    modifier = Modifier
                        .zIndex(topCardZ)
                        .graphicsLayer {
                            translationY = topCardTranslationY
                        }
                        .onSizeChanged {
                            topCardHeightPx = it.height
                        },
                    title = string.converter_from.resource,
                    number = sourceAmountText,
                    codeName = sourceCodeName,
                    rate = sourceRateText,
                    onNumberTap = { displayOffset ->
                        cursorIndex = displayOffsetToRawIndex(
                            displayValue = sourceAmountText,
                            displayOffset = displayOffset
                        ).coerceIn(0, enteredAmount.length)
                    },
                    onRateTap = if (sourceCode == SOM_LABEL) {
                        null
                    } else {
                        {
                            if (currencyOptions.isNotEmpty()) {
                                showCurrencyPicker = true
                            }
                        }
                    }
                )

                ConverterItem(
                    modifier = Modifier
                        .zIndex(bottomCardZ)
                        .graphicsLayer {
                            translationY = bottomCardTranslationY
                        }
                        .onSizeChanged {
                            bottomCardHeightPx = it.height
                        },
                    title = string.converter_to.resource,
                    number = convertedAmount,
                    codeName = targetCodeName,
                    rate = targetRateText,
                    onRateTap = if (targetCode == SOM_LABEL) {
                        null
                    } else {
                        {
                            if (currencyOptions.isNotEmpty()) {
                                showCurrencyPicker = true
                            }
                        }
                    }
                )

                CurrencyIconButton(
                    image = drawable.switch_ic,
                    color = CurrencyColors.bottomBar,
                    tint = CurrencyColors.button,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(switchSize)
                        .zIndex(3f)
                        .graphicsLayer {
                            translationY = (maxCardHeightPx + spacingPx).toFloat()
                            rotationZ = switchRotation
                        },
                    onIconClick = {
                        if (isSwapAnimating) return@CurrencyIconButton
                        isSwapAnimating = true
                        coroutineScope.launch {
                            swapProgress.snapTo(0f)
                            swapProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 360,
                                    easing = FastOutSlowInEasing
                                )
                            )
                            isSelectedCurrencyToSom = !isSelectedCurrencyToSom
                            swapProgress.snapTo(0f)
                            isSwapAnimating = false
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            NumberGridItem(
                keyHeight = keyHeight,
                onAction = { action ->
                    val result = handleKeyboardAction(
                        currentValue = enteredAmount,
                        currentCursorIndex = cursorIndex,
                        action = action
                    )
                    enteredAmount = result.value
                    cursorIndex = result.cursorIndex
                }
            )
            Spacer(modifier = Modifier.height(CurrencyDimensions.medium))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }

        if (showCurrencyPicker) {
            CurrencyPickerDialog(
                currencies = currencyOptions,
                selectedCode = activeCurrency.code,
                onDismiss = { showCurrencyPicker = false },
                onCurrencySelected = { currency ->
                    selectedCurrency = currency
                    showCurrencyPicker = false
                }
            )
        }
    }
}

@Composable
private fun CurrencyPickerDialog(
    currencies: List<ConverterCurrency>,
    selectedCode: String,
    onDismiss: () -> Unit,
    onCurrencySelected: (ConverterCurrency) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CurrencyColors.bottomBar,
        title = {
            Text(
                text = string.converter_select_currency.resource,
                color = CurrencyColors.text,
                style = CurrencyTypography.labelSemiBold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp),
                verticalArrangement = Arrangement.spacedBy(CurrencyDimensions.extraSmall)
            ) {
                items(
                    items = currencies,
                    key = { it.code }
                ) { currency ->
                    val isSelected = currency.code.equals(selectedCode, ignoreCase = true)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onCurrencySelected(currency) },
                        shape = RoundedCornerShape(CurrencyDimensions.small),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) {
                                CurrencyColors.button.copy(alpha = 0.45f)
                            } else {
                                CurrencyColors.textSecondary.copy(alpha = 0.16f)
                            }
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                CurrencyColors.button.copy(alpha = 0.12f)
                            } else {
                                CurrencyColors.bottomBar
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = CurrencyDimensions.small,
                                vertical = CurrencyDimensions.extraSmall
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = currency.code,
                                    color = CurrencyColors.text,
                                    style = CurrencyTypography.labelSemiBold
                                )
                                Text(
                                    text = currency.codeName,
                                    color = CurrencyColors.textSecondary,
                                    style = CurrencyTypography.captionRegular
                                )
                            }
                            Text(
                                text = formatAmountWithCode(
                                    value = currency.rate.convertSomToDouble(),
                                    code = SOM_LABEL
                                ),
                                color = CurrencyColors.textSecondary,
                                style = CurrencyTypography.captionRegular
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = string.common_cancel.resource,
                    color = CurrencyColors.button,
                    style = CurrencyTypography.textSemiBold
                )
            }
        }
    )
}

private data class ConverterCurrency(
    val codeName: String,
    val code: String,
    val rate: String
)

private const val SOM_LABEL = "so'm"
private const val SOM_CURRENCY_CODE = "UZS"
private const val DEFAULT_INPUT = "0"
private const val MAX_INTEGER_DIGITS = 9
private const val MAX_DECIMAL_DIGITS = 4
private const val CURSOR_MARKER = "|"

private val SMALL_SCREEN_THRESHOLD_DP = 700.dp
private val SMALL_KEY_HEIGHT = 64.dp
private val DEFAULT_KEY_HEIGHT = 80.dp
private val SWITCH_BUTTON_SIZE = 44.dp
private val MIN_CARD_HEIGHT_DP = 118.dp

private data class KeyboardEditResult(
    val value: String,
    val cursorIndex: Int
)

private fun CBUModel.toConverterCurrency(): ConverterCurrency = ConverterCurrency(
    codeName = ccyName,
    code = ccy,
    rate = rate
)

private fun handleKeyboardAction(
    currentValue: String,
    currentCursorIndex: Int,
    action: NumberKeyboardAction
): KeyboardEditResult {
    val sanitizedValue = currentValue.ifBlank { DEFAULT_INPUT }
    val sanitizedCursor = currentCursorIndex.coerceIn(0, sanitizedValue.length)

    return when (action) {
        is NumberKeyboardAction.Digit -> insertDigitAtCursor(
            currentValue = sanitizedValue,
            cursorIndex = sanitizedCursor,
            digit = action.value
        )

        NumberKeyboardAction.Decimal -> insertDecimalAtCursor(
            currentValue = sanitizedValue,
            cursorIndex = sanitizedCursor
        )

        NumberKeyboardAction.Backspace -> removeAtCursor(
            currentValue = sanitizedValue,
            cursorIndex = sanitizedCursor
        )

        NumberKeyboardAction.ClearAll -> KeyboardEditResult(
            value = DEFAULT_INPUT,
            cursorIndex = DEFAULT_INPUT.length
        )
    }
}

private fun insertDigitAtCursor(
    currentValue: String,
    cursorIndex: Int,
    digit: String
): KeyboardEditResult {
    if (currentValue == DEFAULT_INPUT && !currentValue.contains(".") && cursorIndex <= 1) {
        return if (digit == DEFAULT_INPUT) {
            KeyboardEditResult(value = DEFAULT_INPUT, cursorIndex = DEFAULT_INPUT.length)
        } else {
            KeyboardEditResult(value = digit, cursorIndex = 1)
        }
    }

    val candidate = buildString(currentValue.length + 1) {
        append(currentValue.substring(0, cursorIndex))
        append(digit)
        append(currentValue.substring(cursorIndex))
    }

    if (!isWithinEditableLimits(candidate)) {
        return KeyboardEditResult(value = currentValue, cursorIndex = cursorIndex)
    }

    return KeyboardEditResult(
        value = candidate,
        cursorIndex = (cursorIndex + 1).coerceAtMost(candidate.length)
    )
}

private fun insertDecimalAtCursor(
    currentValue: String,
    cursorIndex: Int
): KeyboardEditResult {
    if (currentValue.contains(".")) {
        return KeyboardEditResult(value = currentValue, cursorIndex = cursorIndex)
    }

    if (currentValue == DEFAULT_INPUT) {
        return KeyboardEditResult(value = "0.", cursorIndex = 2)
    }

    val (candidate, nextCursor) = if (cursorIndex == 0) {
        "0.$currentValue" to 2
    } else {
        buildString(currentValue.length + 1) {
            append(currentValue.substring(0, cursorIndex))
            append('.')
            append(currentValue.substring(cursorIndex))
        } to (cursorIndex + 1)
    }

    if (!isWithinEditableLimits(candidate)) {
        return KeyboardEditResult(value = currentValue, cursorIndex = cursorIndex)
    }

    return KeyboardEditResult(
        value = candidate,
        cursorIndex = nextCursor.coerceIn(0, candidate.length)
    )
}

private fun removeAtCursor(
    currentValue: String,
    cursorIndex: Int
): KeyboardEditResult {
    if (cursorIndex <= 0) {
        return KeyboardEditResult(value = currentValue, cursorIndex = cursorIndex)
    }

    var updatedValue = buildString(currentValue.length - 1) {
        append(currentValue.substring(0, cursorIndex - 1))
        append(currentValue.substring(cursorIndex))
    }
    var updatedCursor = cursorIndex - 1

    if (updatedValue.isBlank() || updatedValue == ".") {
        return KeyboardEditResult(value = DEFAULT_INPUT, cursorIndex = DEFAULT_INPUT.length)
    }

    if (updatedValue.startsWith(".")) {
        updatedValue = "0$updatedValue"
        updatedCursor += 1
    }

    return KeyboardEditResult(
        value = updatedValue,
        cursorIndex = updatedCursor.coerceIn(0, updatedValue.length)
    )
}

private fun isWithinEditableLimits(value: String): Boolean {
    if (value.isBlank()) return false
    if (value.count { it == '.' } > 1) return false
    if (!value.all { it.isDigit() || it == '.' }) return false

    val integerPart = value.substringBefore('.', missingDelimiterValue = value)
    val decimalPart = value.substringAfter('.', missingDelimiterValue = "")

    if (integerPart.length > MAX_INTEGER_DIGITS) return false
    if (value.contains(".") && decimalPart.length > MAX_DECIMAL_DIGITS) return false

    return true
}

private fun withCursorMarker(
    formattedAmount: String,
    rawCursorIndex: Int
): String {
    val formattedCursorIndex = rawIndexToFormattedOffset(
        formattedValue = formattedAmount,
        rawIndex = rawCursorIndex
    )
    return buildString(formattedAmount.length + 1) {
        append(formattedAmount.substring(0, formattedCursorIndex))
        append(CURSOR_MARKER)
        append(formattedAmount.substring(formattedCursorIndex))
    }
}

private fun rawIndexToFormattedOffset(
    formattedValue: String,
    rawIndex: Int
): Int {
    if (rawIndex <= 0) return 0

    var seenRawChars = 0
    formattedValue.forEachIndexed { index, char ->
        if (char.isDigit() || char == '.') {
            seenRawChars++
            if (seenRawChars == rawIndex) {
                return index + 1
            }
        }
    }
    return formattedValue.length
}

private fun displayOffsetToRawIndex(
    displayValue: String,
    displayOffset: Int
): Int {
    val clampedOffset = displayOffset.coerceIn(0, displayValue.length)
    return displayValue
        .take(clampedOffset)
        .count { it.isDigit() || it == '.' }
}

private fun formatAmountWithCode(
    value: Double,
    code: String,
): String = "${formatAmount(value)} $code"

private fun formatAmount(value: Double): String {
    val safeValue = if (value.isFinite()) value else 0.0
    val normalized = safeValue
        .formatNumberDynamically()
        .replace(",", ".")
        .removeSuffix(".")

    return formatEditableNumber(normalized)
}

private fun formatEditableNumber(value: String): String {
    if (value.isBlank()) return DEFAULT_INPUT

    val hasTrailingDot = value.endsWith(".")
    val splitValue = value.split(".", limit = 2)
    val integerPart = splitValue.firstOrNull().orEmpty().ifBlank { DEFAULT_INPUT }
    val decimalPart = splitValue.getOrNull(1).orEmpty()

    val groupedInteger = integerPart
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    return when {
        hasTrailingDot -> "$groupedInteger."
        decimalPart.isNotEmpty() -> "$groupedInteger.$decimalPart"
        else -> groupedInteger
    }
}

private fun Double.inverseRate(): Double {
    if (!this.isFinite() || this <= 0.0) return 0.0
    return 1 / this
}
