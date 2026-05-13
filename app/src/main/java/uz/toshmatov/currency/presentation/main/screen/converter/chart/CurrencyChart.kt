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
        viewModel.load(currencyCode, 30)
    }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
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
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PeriodChip("7k", state.selectedDays == 7) { viewModel.load(currencyCode, 7) }
                PeriodChip("30k", state.selectedDays == 30) { viewModel.load(currencyCode, 30) }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.isLoading && state.points.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CurrencyColors.button, strokeWidth = 2.dp)
                }
            }
            state.points.size >= 2 -> {
                LineChart(
                    points = state.points,
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) accent.copy(alpha = 0.12f) else CurrencyColors.itemBackground)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) accent else accent.copy(alpha = 0f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
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
private fun LineChart(points: List<CurrencyChartPoint>, modifier: Modifier = Modifier) {
    val accent = CurrencyColors.button
    val progress = remember(points) { Animatable(0f) }

    LaunchedEffect(points) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(800))
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
                colors = listOf(accent.copy(alpha = 0.3f), accent.copy(alpha = 0f)),
                startY = 0f, endY = h
            )
        )
        drawPath(path = linePath, color = accent, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round))

        val lx = xOf(visible.size - 1)
        val ly = yOf(visible.last().rate)
        drawCircle(color = accent.copy(alpha = 0.25f), radius = 8.dp.toPx(), center = Offset(lx, ly))
        drawCircle(color = accent, radius = 4.dp.toPx(), center = Offset(lx, ly))
    }
}