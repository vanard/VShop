package com.vanard.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val defaultModifier = Modifier.fillMaxWidth()

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 3,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = defaultModifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val isOverflow = textLayoutResult?.hasVisualOverflow == true
    val scrollState = rememberScrollState()

    val greyText = Color(0xff787676)

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            )
    ) {
        if (expanded) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                NormalText(
                    text = text,
                    style = style.copy(color = greyText),
                    onTextLayout = { textLayoutResult = it }
                )
            }
        } else {
            NormalText(
                text = text,
                maxLines = minimizedMaxLines,
                onTextLayout = { textLayoutResult = it },
                style = style.copy(color = greyText)
            )
        }

        if (isOverflow || expanded) {
            NormalText(
                text = if (expanded) "Read less" else "Read more..",
                style = style.copy(color = Color.Black, fontWeight = FontWeight.SemiBold),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }
}

@Preview
@Composable
fun ExpandableTextPreview(modifier: Modifier = Modifier) {
    ExpandableText("AUU")
}