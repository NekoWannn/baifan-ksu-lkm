package me.weishu.kernelsu.ui.screen.token

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.weishu.kernelsu.R
import me.weishu.kernelsu.ui.component.deepSeekHeroBrush
import me.weishu.kernelsu.ui.theme.DeepSeekBlue
import me.weishu.kernelsu.ui.theme.isInDarkTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TokenPager(
    bottomInnerPadding: Dp,
    isCurrentPage: Boolean,
) {
    val context = LocalContext.current
    val store = remember(context) { TokenKeyStore(context) }
    val scope = rememberCoroutineScope()
    val dark = isInDarkTheme()
    val ink = if (dark) Color(0xFFF5F8FF) else Color(0xFF111A33)
    val muted = if (dark) Color(0xFFB9C8EA) else Color(0xFF44557D)
    val fieldBackground = if (dark) Color(0x33FFFFFF) else Color(0x66FFFFFF)
    val keyCardBrush = if (dark) {
        Brush.linearGradient(listOf(Color(0xFF1B1638), Color(0xFF26325E)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFF7F3FF), Color(0xFFE2E9FF)))
    }

    var apiKey by remember { mutableStateOf(store.load()) }
    var showKey by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<TokenBalanceResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var updatedAt by remember { mutableStateOf("") }
    var queryToken by remember { mutableStateOf(0) }

    fun query() {
        if (apiKey.isBlank() || loading) return
        store.save(apiKey)
        queryToken++
    }

    LaunchedEffect(queryToken, isCurrentPage) {
        if (queryToken == 0 && (!isCurrentPage || apiKey.isBlank() || result != null)) return@LaunchedEffect
        if (apiKey.isBlank()) return@LaunchedEffect
        loading = true
        error = null
        TokenBalanceRepository.query(apiKey)
            .onSuccess {
                result = it
                updatedAt = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            }
            .onFailure {
                error = it.message ?: it.javaClass.simpleName
            }
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                )
            )
            .padding(horizontal = 22.dp)
            .padding(top = 18.dp, bottom = bottomInnerPadding + 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.Bottom,
        ) {
            val portraitShape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomEnd = 6.dp,
                bottomStart = 0.dp,
            )
            Image(
                painter = painterResource(R.drawable.token_baifan),
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp)
                    .clip(portraitShape),
                contentScale = ContentScale.Fit,
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BasicText(
                    text = stringResource(R.string.token),
                    style = TextStyle(
                        color = ink,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                SpeechBubble(
                    text = stringResource(R.string.token_baifan_line),
                    ink = ink,
                    dark = dark,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(deepSeekHeroBrush())
                .padding(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText(
                        text = stringResource(R.string.token_balance_title),
                        style = TextStyle(
                            color = ink.copy(alpha = 0.78f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(DeepSeekBlue.copy(alpha = 0.16f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        BasicText(
                            text = when {
                                loading -> stringResource(R.string.token_querying)
                                result?.available == true -> stringResource(R.string.token_available)
                                result != null -> stringResource(R.string.token_unavailable)
                                else -> stringResource(R.string.token_not_queried)
                            },
                            style = TextStyle(
                                color = DeepSeekBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                }

                val balances = result?.balances.orEmpty()
                if (balances.isEmpty()) {
                    BasicText(
                        text = if (loading) "…" else "—",
                        style = TextStyle(
                            color = ink,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                } else {
                    balances.forEach { balance ->
                        BasicText(
                            text = "${currencySymbol(balance.currency)}${balance.total}",
                            style = TextStyle(
                                color = ink,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            BalanceMeta(
                                label = stringResource(R.string.token_granted),
                                value = "${currencySymbol(balance.currency)}${balance.granted}",
                                color = muted,
                            )
                            BalanceMeta(
                                label = stringResource(R.string.token_topped_up),
                                value = "${currencySymbol(balance.currency)}${balance.toppedUp}",
                                color = muted,
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText(
                        text = if (updatedAt.isBlank()) {
                            stringResource(R.string.token_no_key)
                        } else {
                            stringResource(R.string.token_last_updated, updatedAt)
                        },
                        style = TextStyle(color = muted, fontSize = 11.sp),
                        modifier = Modifier.weight(1f),
                    )
                    if (apiKey.isNotBlank()) {
                        TokenPillButton(
                            text = stringResource(R.string.token_refresh),
                            enabled = !loading,
                            onClick = { query() },
                        )
                    }
                }
            }
        }

        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (dark) Color(0x33FF6B6B) else Color(0x22D93025))
                    .padding(14.dp)
            ) {
                BasicText(
                    text = stringResource(R.string.token_error, error.orEmpty()),
                    style = TextStyle(
                        color = if (dark) Color(0xFFFFB4AB) else Color(0xFFB3261E),
                        fontSize = 12.sp,
                    ),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(keyCardBrush)
                .border(
                    BorderStroke(
                        1.dp,
                        if (dark) Color(0x33A9BCFF) else Color(0x335B7CFA),
                    ),
                    RoundedCornerShape(24.dp),
                )
                .padding(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BasicText(
                    text = stringResource(R.string.token_api_key),
                    style = TextStyle(
                        color = ink,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                BasicTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    singleLine = true,
                    textStyle = TextStyle(color = ink, fontSize = 14.sp),
                    cursorBrush = SolidColor(DeepSeekBlue),
                    visualTransformation = if (showKey) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(fieldBackground)
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (apiKey.isEmpty()) {
                                BasicText(
                                    text = "sk-…",
                                    style = TextStyle(color = muted, fontSize = 14.sp),
                                )
                            }
                            innerTextField()
                        }
                    },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TokenPillButton(
                        text = stringResource(R.string.token_save_query),
                        enabled = apiKey.isNotBlank() && !loading,
                        onClick = { query() },
                        modifier = Modifier.weight(1f),
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { showKey = !showKey }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        BasicText(
                            text = stringResource(
                                if (showKey) R.string.token_hide else R.string.token_show
                            ),
                            style = TextStyle(color = muted, fontSize = 12.sp),
                        )
                    }
                }
                BasicText(
                    text = stringResource(R.string.token_clear),
                    style = TextStyle(
                        color = muted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    modifier = Modifier.clickable {
                        store.clear()
                        apiKey = ""
                        result = null
                        error = null
                        updatedAt = ""
                    },
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(if (dark) Color(0x22131E3A) else Color(0x99FFFFFF))
                .clickable {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://platform.deepseek.com/api_keys"),
                        )
                    )
                }
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                BasicText(
                    text = stringResource(R.string.token_get_key),
                    style = TextStyle(
                        color = DeepSeekBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                BasicText(
                    text = stringResource(R.string.token_key_storage_note),
                    style = TextStyle(color = muted, fontSize = 11.sp),
                )
            }
        }
    }
}

@Composable
private fun SpeechBubble(text: String, ink: Color, dark: Boolean) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 18.dp,
                    bottomEnd = 18.dp,
                    bottomStart = 18.dp,
                )
            )
            .background(if (dark) Color(0xEE1B2A4A) else Color(0xF2FFFFFF))
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                color = ink,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
            ),
        )
    }
}

@Composable
private fun BalanceMeta(label: String, value: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        BasicText(
            text = label,
            style = TextStyle(color = color, fontSize = 10.sp),
        )
        BasicText(
            text = value,
            style = TextStyle(
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun TokenPillButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) DeepSeekBlue else DeepSeekBlue.copy(alpha = 0.35f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

private fun currencySymbol(currency: String): String = when (currency.uppercase(Locale.ROOT)) {
    "CNY" -> "¥"
    "USD" -> "$"
    else -> "$currency "
}
