package com.music.purelymusic.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.purelymusic.ui.theme.AMPlayerTheme

/** Keep this object as the sole editable source for the first-launch update sheet. */
object ReleaseNotes {
    const val version = "3.0"
    val zhHighlights = listOf(
        Triple(Icons.Default.AutoAwesome, "Liquid Glass 视觉系统", "导航、播放条与核心控件，换上轻盈的玻璃材质与触感反馈。"),
        Triple(Icons.Default.Translate, "歌词翻译，更完整", "自动补翻遗漏的句子，让每一行歌词都跟得上。"),
        Triple(Icons.Default.GraphicEq, "声音，更合你的心意", "新增均衡器场景预设，频段调节也更直观。")
    )
    val enHighlights = listOf(
        Triple(Icons.Default.AutoAwesome, "Meet Liquid Glass", "Lighter glass surfaces and tactile feedback for navigation, now playing, and controls."),
        Triple(Icons.Default.Translate, "Every lyric, translated", "Automatic retries fill in missing lines so you can follow every word."),
        Triple(Icons.Default.GraphicEq, "Find your sound", "New equalizer presets and clearer controls to tune every band.")
    )
}

@Composable
fun WhatsNewSheet(language: String, onDismiss: () -> Unit) {
    val chinese = language == "zh"
    val colors = MaterialTheme.colorScheme
    val dark = colors.surface.luminance() < 0.5f
    val items = if (chinese) ReleaseNotes.zhHighlights else ReleaseNotes.enHighlights

    LiquidGlass(
        modifier = Modifier.widthIn(max = 440.dp).fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .semantics { paneTitle = if (chinese) "${ReleaseNotes.version} 更新亮点" else "What's new in ${ReleaseNotes.version}" },
        shape = RoundedCornerShape(32.dp),
        dark = dark,
        opacity = 0.96f,
        highlightAlpha = if (dark) 0.08f else 0.38f,
        edgeAlpha = if (dark) 0.16f else 0.42f
    ) {
        BoxWithConstraints {
            val compact = maxHeight < 600.dp || LocalDensity.current.fontScale > 1.3f
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 12.dp, top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(30.dp).background(colors.primary.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MusicNote, contentDescription = null, tint = colors.primary, modifier = Modifier.size(19.dp))
                    }
                    Text(
                        text = "Purely Music",
                        modifier = Modifier.weight(1f).padding(start = 10.dp),
                        color = colors.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = if (chinese) "关闭更新亮点" else "Close release highlights",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Keep the action visible when the window is short or the system font is enlarged.
                Column(
                    modifier = Modifier.weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
                ) {
                    ReleaseCover(chinese = chinese, dark = dark, compact = compact)
                    Text(
                        text = if (chinese) "听感，焕然一新。" else "A fresh way to listen.",
                        modifier = Modifier.semantics { heading() },
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (compact) 24.sp else 27.sp,
                        lineHeight = if (compact) 32.sp else 34.sp,
                        letterSpacing = (-0.6).sp
                    )
                    if (!compact) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (chinese) "从界面到声音，让喜欢的音乐更动人。" else "Thoughtful details, from the first tap to the last note.",
                            color = colors.onSurfaceVariant,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = if (compact) 16.dp else 24.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (chinese) "本次更新" else "What's new",
                            modifier = Modifier.semantics { heading() },
                            color = colors.onSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.6.sp
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = colors.outlineVariant.copy(alpha = 0.65f))
                    }
                    items.forEachIndexed { index, (icon, title, detail) ->
                        ReleaseHighlight(icon = icon, title = title, detail = detail)
                        if (index < items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 54.dp),
                                color = colors.outlineVariant.copy(alpha = 0.45f)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp)
                    ) {
                        Text(
                            text = if (chinese) "开始体验" else "Explore now",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReleaseCover(chinese: Boolean, dark: Boolean, compact: Boolean) {
    val colors = MaterialTheme.colorScheme
    val fontScale = LocalDensity.current.fontScale
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(top = if (compact) 8.dp else 14.dp, bottom = if (compact) 14.dp else 18.dp)) {
        val showArtwork = !compact && maxWidth >= 260.dp && fontScale <= 1.3f
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ReleaseNotes.version,
                    color = colors.primary,
                    fontSize = if (compact) 44.sp else 76.sp,
                    lineHeight = if (compact) 48.sp else 80.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = if (compact) (-2).sp else (-4).sp
                )
                if (!compact) {
                    Text(
                        text = if (chinese) "为每一次聆听" else "Made for listening",
                        modifier = Modifier.padding(top = 4.dp),
                        color = colors.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            if (showArtwork) ReleaseArtwork(dark = dark, modifier = Modifier.size(120.dp))
        }
    }
}

/** A decorative record and glass waveform; no fake playback controls or external assets. */
@Composable
private fun ReleaseArtwork(dark: Boolean, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val accent = colors.primary
    val groove = colors.onSurface.copy(alpha = if (dark) 0.10f else 0.07f)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.radialGradient(listOf(accent.copy(alpha = 0.18f), Color.Transparent)), CircleShape
            )
        )
        Canvas(modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp).size(88.dp)) {
            drawCircle(color = accent.copy(alpha = if (dark) 0.12f else 0.08f))
            listOf(0.92f, 0.78f, 0.64f, 0.50f).forEach { fraction ->
                drawCircle(color = groove, radius = size.minDimension * fraction / 2, style = Stroke(width = 1.dp.toPx()))
            }
            drawCircle(color = accent.copy(alpha = 0.82f), radius = size.minDimension * 0.17f)
            drawCircle(color = colors.surface, radius = 3.dp.toPx())
        }
        Box(
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 15.dp)
                .size(width = 112.dp, height = 64.dp).rotate(-9f).clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(colors.surfaceContainerHigh.copy(alpha = 0.95f), colors.surface.copy(alpha = 0.78f))
                    )
                )
                .border(1.dp, colors.onSurface.copy(alpha = 0.10f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(width = 72.dp, height = 30.dp)) {
                val heights = listOf(0.25f, 0.48f, 0.76f, 0.54f, 1f, 0.72f, 0.44f, 0.64f, 0.30f)
                val step = size.width / heights.size
                heights.forEachIndexed { index, fraction ->
                    val barHeight = size.height * fraction
                    drawRoundRect(
                        color = accent.copy(alpha = if (index == 4) 1f else 0.65f),
                        topLeft = Offset(index * step + step * 0.2f, (size.height - barHeight) / 2),
                        size = Size(step * 0.55f, barHeight),
                        cornerRadius = CornerRadius(step / 2)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReleaseHighlight(icon: ImageVector, title: String, detail: String) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp).semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.padding(top = 2.dp).size(40.dp)
                .background(colors.primary.copy(alpha = 0.09f), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = colors.primary, modifier = Modifier.size(21.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = colors.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 21.sp)
            Text(detail, color = colors.onSurfaceVariant, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

@Preview(name = "更新亮点 · 浅色", widthDp = 390, heightDp = 844, showBackground = true)
@Preview(name = "更新亮点 · 小屏大字体", widthDp = 320, heightDp = 568, fontScale = 1.5f, showBackground = true)
@Composable
private fun WhatsNewLightPreview() {
    AMPlayerTheme(darkTheme = false) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            WhatsNewSheet(language = "zh", onDismiss = {})
        }
    }
}

@Preview(name = "Release highlights · Dark", widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun WhatsNewDarkPreview() {
    AMPlayerTheme(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            WhatsNewSheet(language = "en", onDismiss = {})
        }
    }
}
