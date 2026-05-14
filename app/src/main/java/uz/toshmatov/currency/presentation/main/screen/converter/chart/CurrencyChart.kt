package uz.toshmatov.currency.presentation.main.screen.converter.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.domain.model.CurrencyChartPoint
import kotlin.math.abs

@Composable
fun CurrencyChartSection(
    currencyCode: String,
    viewModel: ChartViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(currencyCode) {
        viewModel.load(currencyCode, ChartPeriod.DAYS_7)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kurs tarixi",
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.text
            )
        }

        Spacer(Modifier.height(8.dp))

        // Period chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(ChartPeriod.entries) { period ->
                PeriodChip(
                    label = period.label,
                    selected = state.period == period,
                    onClick = { viewModel.load(currencyCode, period) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        when {
            state.isLoading -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CurrencyColors.button, strokeWidth = 2.dp)
            }

            state.points.size >= 2 -> {
                val first = state.points.first().rate
                val last = state.points.last().rate
                val diff = last - first
                val diffPct = if (first > 0f) (diff / first * 100f) else 0f
                val isUp = diff >= 0f
                val diffColor = if (isUp) CurrencyColors.success else CurrencyColors.error

                // Current rate + change row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "%,.2f so'm".format(last),
                            style = CurrencyTypography.textSemiBold,
                            color = CurrencyColors.button
                        )
                        Text(
                            text = "${if (isUp) "▲" else "▼"} %,.2f  (%.2f%%)".format(
                                abs(diff), abs(diffPct)
                            ),
                            style = CurrencyTypography.captionRegular,
                            color = diffColor
                        )
                    }
                    Text(
                        text = "${state.points.first().date.take(5)} — ${state.points.last().date.take(5)}",
                        style = CurrencyTypography.captionRegular,
                        color = CurrencyColors.textSecondary
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Chart
                LineChart(
                    points = state.points,
                    isUp = isUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(160.dp)
                )
            }
        }
    }
}

@Composable
private fun PeriodChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val accent = CurrencyColors.button
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) accent else CurrencyColors.itemBackground)
            .border(
                1.dp,
                if (selected) accent else CurrencyColors.itemBackground,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = CurrencyTypography.captionRegular,
            color = if (selected) Color.White else CurrencyColors.textSecondary
        )
    }
}

@Composable
private fun LineChart(
    points: List<CurrencyChartPoint>,
    isUp: Boolean,
    modifier: Modifier = Modifier
) {
    val lineColor = if (isUp) CurrencyColors.success else CurrencyColors.error
    val gridColor = CurrencyColors.textSecondary.copy(alpha = 0.15f)
    val labelColor = CurrencyColors.textSecondary
    val textMeasurer = rememberTextMeasurer()
    val progress = remember(points) { Animatable(0f) }

    LaunchedEffect(points) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(800))
    }

    val minRate = points.minOf { it.rate }
    val maxRate = points.maxOf { it.rate }
    val range = (maxRate - minRate).takeIf { it > 0f } ?: 1f

    val labelStyle = TextStyle(fontSize = 10.sp, color = labelColor)

    Canvas(modifier = modifier) {
        val yAxisWidth = 70.dp.toPx()
        val chartW = size.width - yAxisWidth
        val chartH = size.height - 16.dp.toPx()
        val offsetY = 8.dp.toPx()
        val count = points.size

        fun xOf(i: Int) = yAxisWidth + i * chartW / (count - 1).coerceAtLeast(1)
        fun yOf(rate: Float) = offsetY + chartH - ((rate - minRate) / range) * chartH * 0.9f

        // Horizontal grid lines (min, mid, max)
        val gridLevels = listOf(minRate, (minRate + maxRate) / 2f, maxRate)
        gridLevels.forEach { rate ->
            val y = yOf(rate)
            // Grid line
            drawLine(
                color = gridColor,
                start = Offset(yAxisWidth, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
            )
            // Y-axis label
            val label = "%,.0f".format(rate)
            val measured = textMeasurer.measure(label, labelStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = label,
                style = labelStyle,
                topLeft = Offset(0f, y - measured.size.height / 2f)
            )
        }

        // Animate: draw only up to progress
        val animatedCount = (count * progress.value).toInt().coerceAtLeast(2)
        val visible = points.take(animatedCount)

        val linePath = Path()
        val fillPath = Path()

        visible.forEachIndexed { i, point ->
            val x = xOf(i)
            val y = yOf(point.rate)
            if (i == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, offsetY + chartH)
                fillPath.lineTo(x, y)
            } else {
                val prevX = xOf(i - 1)
                val prevY = yOf(visible[i - 1].rate)
                val cx = (prevX + x) / 2f
                linePath.cubicTo(cx, prevY, cx, y, x, y)
                fillPath.cubicTo(cx, prevY, cx, y, x, y)
            }
        }
        fillPath.lineTo(xOf(visible.size - 1), offsetY + chartH)
        fillPath.close()

        // Gradient fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.22f), lineColor.copy(alpha = 0f)),
                startY = offsetY, endY = offsetY + chartH
            )
        )

        // Line
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Last point dot
        val lx = xOf(visible.size - 1)
        val ly = yOf(visible.last().rate)
        drawCircle(color = lineColor.copy(alpha = 0.2f), radius = 7.dp.toPx(), center = Offset(lx, ly))
        drawCircle(color = lineColor, radius = 3.5.dp.toPx(), center = Offset(lx, ly))
    }
}