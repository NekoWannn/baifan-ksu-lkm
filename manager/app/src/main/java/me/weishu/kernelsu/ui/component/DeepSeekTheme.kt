package me.weishu.kernelsu.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.weishu.kernelsu.R
import me.weishu.kernelsu.ui.theme.DeepSeekBlue
import me.weishu.kernelsu.ui.theme.isInDarkTheme
import kotlin.random.Random

private val DeepSeekInk = Color(0xFF111A33)
private val DeepSeekLightText = Color(0xFFF5F8FF)
private val DeepSeekMutedLight = Color(0xFF44557D)
private val DeepSeekMutedDark = Color(0xFFB9C8EA)

@Composable
fun deepSeekHeroBrush(): Brush =
    if (isInDarkTheme()) {
        Brush.linearGradient(listOf(Color(0xFF101A38), Color(0xFF243B78)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFF1F5FF), Color(0xFFC9DAFF)))
    }

@Composable
fun Modifier.deepSeekCardGradient(shape: Shape = RoundedCornerShape(16.dp)): Modifier =
    clip(shape).background(deepSeekHeroBrush())

@Composable
fun DeepSeekHeroCard(modifier: Modifier = Modifier) {
    val dark = isInDarkTheme()
    val titleColor = if (dark) DeepSeekLightText else DeepSeekInk
    val mutedColor = if (dark) DeepSeekMutedDark else DeepSeekMutedLight
    val background = deepSeekHeroBrush()
    val badgeColor = if (dark) Color(0xFF9DB6FF) else DeepSeekBlue
    val quotes = stringArrayResource(R.array.deepseek_quotes)
    var quoteIndex by remember {
        mutableIntStateOf(if (quotes.isEmpty()) 0 else Random.nextInt(quotes.size))
    }
    val quote = quotes.getOrNull(quoteIndex).orEmpty()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(193.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(background),
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-44).dp, y = (-54).dp)
                .background(Color.White.copy(alpha = if (dark) 0.06f else 0.28f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.BottomStart)
                .offset(x = 84.dp, y = 54.dp)
                .background(DeepSeekBlue.copy(alpha = if (dark) 0.18f else 0.10f), CircleShape),
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, top = 18.dp, bottom = 16.dp, end = 4.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(badgeColor, CircleShape),
                    )
                    Spacer(Modifier.width(6.dp))
                    BasicText(
                        text = stringResource(R.string.deepseek_hero_badge),
                        style = TextStyle(
                            color = badgeColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp,
                        ),
                    )
                }
                Spacer(Modifier.height(9.dp))
                BasicText(
                    text = stringResource(R.string.deepseek_hero_title),
                    style = TextStyle(
                        color = titleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.4).sp,
                    ),
                )
                Spacer(Modifier.height(3.dp))
                BasicText(
                    text = stringResource(R.string.deepseek_hero_subtitle),
                    style = TextStyle(
                        color = mutedColor,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Spacer(Modifier.height(9.dp))
                BasicText(
                    text = "「$quote」",
                    style = TextStyle(
                        color = titleColor.copy(alpha = 0.9f),
                        fontSize = 13.5.sp,
                        lineHeight = 18.5.sp,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(7.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(DeepSeekBlue.copy(alpha = if (dark) 0.28f else 0.12f))
                        .clickable {
                            if (quotes.size > 1) {
                                var next = Random.nextInt(quotes.size - 1)
                                if (next >= quoteIndex) next++
                                quoteIndex = next
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    BasicText(
                        text = stringResource(R.string.deepseek_quote_switch),
                        style = TextStyle(
                            color = if (dark) Color(0xFFC7D6FF) else DeepSeekBlue,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(126.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Image(
                    painter = painterResource(R.drawable.deepseek_plush),
                    contentDescription = null,
                    modifier = Modifier
                        .height(134.dp)
                        .width(126.dp)
                        .offset(x = 8.dp, y = 16.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
fun DeepSeekAppBackground(content: @Composable () -> Unit) {
    val dark = isInDarkTheme()
    val brush = if (dark) {
        Brush.verticalGradient(
            listOf(Color(0xFF081126), Color(0xFF0C1834), Color(0xFF081126)),
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFFF7FAFF), Color(0xFFE8F1FF), Color(0xFFF7FAFF)),
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush),
    ) {
        content()
    }
}

@Composable
fun deepSeekCardColor(): Color =
    if (isInDarkTheme()) Color(0xFF132247) else Color(0xFFE9F1FF)

@Composable
fun DeepSeekDeviceHintCard(
    deviceModel: String,
    modifier: Modifier = Modifier,
) {
    val dark = isInDarkTheme()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(deepSeekCardColor())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(42.dp)
                .background(DeepSeekBlue, RoundedCornerShape(2.dp)),
        )
        Spacer(Modifier.width(10.dp))
        Column {
            BasicText(
                text = stringResource(R.string.deepseek_device_hint),
                style = TextStyle(
                    color = if (dark) Color(0xFFDCE6FF) else Color(0xFF22335C),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
            Spacer(Modifier.height(5.dp))
            BasicText(
                text = deviceModel,
                style = TextStyle(
                    color = if (dark) DeepSeekLightText else DeepSeekInk,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
fun DeepSeekCreditCard(modifier: Modifier = Modifier) {
    val dark = isInDarkTheme()
    val background = deepSeekCardColor()
    val titleColor = if (dark) DeepSeekLightText else DeepSeekInk
    val mutedColor = if (dark) DeepSeekMutedDark else DeepSeekMutedLight

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.deepseek_plush),
            contentDescription = null,
            modifier = Modifier.size(78.dp),
            contentScale = ContentScale.Fit,
        )
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = stringResource(R.string.deepseek_credit_title),
                style = TextStyle(
                    color = titleColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Spacer(Modifier.height(4.dp))
            BasicText(
                text = stringResource(R.string.deepseek_credit_summary),
                style = TextStyle(
                    color = mutedColor,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                ),
            )
            Spacer(Modifier.height(3.dp))
            BasicText(
                text = stringResource(R.string.deepseek_credit_author),
                style = TextStyle(
                    color = if (dark) Color(0xFF9FB6FF) else DeepSeekBlue,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}
