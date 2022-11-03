package work.racka.reluct.android.compose.components.textfields.texts

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HighlightedText(
    fullText: String,
    highlights: ImmutableList<HighlightTextProps>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fullTextColor: Color = LocalContentColor.current,
    textStyle: TextStyle = LocalTextStyle.current,
    highlightTextDecoration: TextDecoration = TextDecoration.None,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val annotatedString by remember(fullText, highlights) {
        derivedStateOf {
            buildAnnotatedString {
                append(fullText)
                addStyle(
                    style = SpanStyle(
                        color = fullTextColor,
                        fontSize = textStyle.fontSize,
                        fontWeight = textStyle.fontWeight,
                        fontStyle = textStyle.fontStyle,
                        fontSynthesis = textStyle.fontSynthesis,
                        fontFamily = textStyle.fontFamily,
                        letterSpacing = textStyle.letterSpacing,
                        fontFeatureSettings = textStyle.fontFeatureSettings,
                        baselineShift = textStyle.baselineShift,
                        textGeometricTransform = textStyle.textGeometricTransform,
                        localeList = textStyle.localeList,
                        background = textStyle.background,
                        textDecoration = highlightTextDecoration,
                        shadow = textStyle.shadow,

                    ),
                    start = 0,
                    end = fullText.lastIndex
                )
                highlights.forEach { link ->
                    val startIndex = fullText.indexOf(link.text)
                    val endIndex = startIndex + link.text.length
                    addStyle(
                        style = SpanStyle(
                            color = link.color,
                            fontSize = textStyle.fontSize,
                            fontWeight = textStyle.fontWeight,
                            fontStyle = textStyle.fontStyle,
                            fontSynthesis = textStyle.fontSynthesis,
                            fontFamily = textStyle.fontFamily,
                            letterSpacing = textStyle.letterSpacing,
                            fontFeatureSettings = textStyle.fontFeatureSettings,
                            baselineShift = textStyle.baselineShift,
                            textGeometricTransform = textStyle.textGeometricTransform,
                            localeList = textStyle.localeList,
                            background = textStyle.background,
                            textDecoration = highlightTextDecoration,
                            shadow = textStyle.shadow,

                        ),
                        start = startIndex,
                        end = endIndex
                    )
                }
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}
