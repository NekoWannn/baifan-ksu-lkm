package me.weishu.kernelsu.ui.screen.token

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.weishu.kernelsu.ksuApp
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class TokenBalance(
    val currency: String,
    val total: String,
    val granted: String,
    val toppedUp: String,
)

data class TokenBalanceResult(
    val available: Boolean,
    val balances: List<TokenBalance>,
)

class TokenKeyStore(context: Context) {
    private val prefs = context.getSharedPreferences("deepseek_token_balance", Context.MODE_PRIVATE)

    fun load(): String = prefs.getString(KEY_API_KEY, "").orEmpty()

    fun save(key: String) {
        prefs.edit().putString(KEY_API_KEY, key.trim()).apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_API_KEY).apply()
    }

    private companion object {
        const val KEY_API_KEY = "api_key"
    }
}

object TokenBalanceRepository {
    private const val ENDPOINT = "https://api.deepseek.com/user/balance"

    suspend fun query(apiKey: String): Result<TokenBalanceResult> = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder()
                .url(ENDPOINT)
                .header("Authorization", "Bearer ${apiKey.trim()}")
                .header("Accept", "application/json")
                .get()
                .build()

            ksuApp.okhttpClient.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    throw IOException(apiErrorMessage(body) ?: "HTTP ${response.code}")
                }

                val json = JSONObject(body)
                val infos: JSONArray = json.optJSONArray("balance_infos") ?: JSONArray()
                val balances = buildList {
                    for (index in 0 until infos.length()) {
                        val item = infos.optJSONObject(index) ?: continue
                        add(
                            TokenBalance(
                                currency = item.optString("currency", "CNY"),
                                total = item.optString("total_balance", "0"),
                                granted = item.optString("granted_balance", "0"),
                                toppedUp = item.optString("topped_up_balance", "0"),
                            )
                        )
                    }
                }

                TokenBalanceResult(
                    available = json.optBoolean("is_available", false),
                    balances = balances,
                )
            }
        }
    }

    private fun apiErrorMessage(body: String): String? {
        return runCatching {
            val json = JSONObject(body)
            json.optJSONObject("error")?.optString("message")?.takeIf { it.isNotBlank() }
                ?: json.optString("message").takeIf { it.isNotBlank() }
        }.getOrNull()
    }
}
