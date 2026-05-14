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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.domain.model.CurrencyChartPoint

@Composable
fun CurrencyChartSection(
    currencyCode: String,
    viewModel: ChartViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(currencyCode) {
        viewModel.load(currencyCode, ChartPeriod.MONTH_1)
    }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        // Header: title + period chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kurs tarixi",
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.text
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ChartPeriod.entries.forEach { period ->
                    PeriodChip(
                        label = period.label,
                        selected = state.period == period,
                        onClick = { viewModel.load(currencyCode, period) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CurrencyColors.button, strokeWidth = 2.dp)
                }
            }
            state.points.size >= 2 -> {
                // Diff info row
                val first = state.points.first().rate
                val last = state.points.last().rate
                val diff = last - first
                val diffPct = if (first > 0f) (diff / first * 100f) else 0f
                val isUp = diff >= 0f
                val diffColor = if (isUp) CurrencyColors.success else CurrencyColors.error

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${if (isUp) "▲" else "▼"} %,.2f so'm (%.2f%%)".format(
                            kotlin.math.abs(diff), kotlin.math.abs(diffPct)
                        ),
                        style = CurrencyTypography.captionRegular,
                        color = diffColor
                    )
                    Text(
                        text = "%,.2f so'm".format(last),
                        style = CurrencyTypography.textSemiBold,
                        color = CurrencyColors.button
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LineChart(
                    points = state.points,
                    isUp = isUp,
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = state.points.first().date.take(5),
                        style = CurrencyTypography.captionRegular,
                        color = CurrencyColors.textSecondary
                    )
                    Text(
                        text = state.points.last().date.take(5),
                        style = CurrencyTypography.captionRegular,
                        color = CurrencyColors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val accent = CurrencyColors.button
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) accent.copy(alpha = 0.12f) else CurrencyColors.background)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) accent else accent.copy(alpha = 0f),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = CurrencyTypography.captionRegular,
            color = if (selected) accent else CurrencyColors.textSecondary
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
    val progress = remember(points) { Animatable(0f) }

    LaunchedEffect(points) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(900))
    }

    val minRate = points.minOf { it.rate }
    val maxRate = points.maxOf { it.rate }
    val range = (maxRate - minRate).takeIf { it > 0f } ?: 1f

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val count = points.size
        val animatedCount = (count * progress.value).toInt().coerceAtLeast(2)
        val visible = points.take(animatedCount)

        fun xOf(i: Int) = i * w / (count - 1)
        fun yOf(rate: Float) = h - ((rate - minRate) / range) * h * 0.85f - h * 0.05f

        val linePath = Path()
        val fillPath = Path()

        visible.forEachIndexed { i, point ->
            val x = xOf(i)
            val y = yOf(point.rate)
            if (i == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, h)
                fillPath.lineTo(x, y)
            } else {
                val prevX = xOf(i - 1)
                val prevY = yOf(visible[i - 1].rate)
                val cx = (prevX + x) / 2f
                linePath.cubicTo(cx, prevY, cx, y, x, y)
                fillPath.cubicTo(cx, prevY, cx, y, x, y)
            }
        }
        fillPath.lineTo(xOf(visible.size - 1), h)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0f)),
                startY = 0f, endY = h
            )
        )
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        val lx = xOf(visible.size - 1)
        val ly = yOf(visible.last().rate)
        drawCircle(color = lineColor.copy(alpha = 0.2f), radius = 8.dp.toPx(), center = Offset(lx, ly))
        drawCircle(color = lineColor, radius = 4.dp.toPx(), center = Offset(lx, ly))
    }
}