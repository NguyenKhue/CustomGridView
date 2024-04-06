package com.khue.customgridview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khue.customgridview.ui.theme.CustomGridViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomGridViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        VerticalGrid(
                            modifier = Modifier.padding(8.dp),
                            columns = 2,
                            horizontalSpacing = 5.dp,
                            verticalSpacing = 5.dp
                        ) {
                            repeat(25) {
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .background(if (it % 2 == 1) Color.Gray else Color.DarkGray)
                                ) {
                                    Text(
                                        text = "${it + 1}",
                                        color = White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val totalHorizontalSpace = (columns - 1) * horizontalSpacingPx
        val itemWidth = (constraints.maxWidth - totalHorizontalSpace) / columns
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        val placeables = measurables.map { it.measure(itemConstraints) }
        val width = constraints.maxWidth
        val gridHeights = mutableMapOf<Int, Int>()

        placeables.forEachIndexed { index, placeable ->
            val currentGrid = index / columns
            Log.d("VerticalGrid", "currentGrid: $currentGrid")
            val currentGridHeight = gridHeights[currentGrid] ?: 0
            if (placeable.height >= currentGridHeight) gridHeights[currentGrid] = placeable.height
        }

        val verticalSpacingPx = verticalSpacing.roundToPx()
        val totalVerticalSpace = (gridHeights.size - 1) * verticalSpacingPx
        val height = gridHeights.values.sumOf { it } + totalVerticalSpace

        layout(width, height) {
            val itemWidthWithHorizontalSpacing = itemWidth + horizontalSpacingPx
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                placeable.placeRelative(
                    x = column * itemWidthWithHorizontalSpacing,
                    y = columnY[column]
                )
                columnY[column] += placeable.height + verticalSpacingPx
            }
        }
    }
}