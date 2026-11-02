package dev.glimmer.ai.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Minimal OpenAI-compatible chat client. Credentials are supplied per request and never persisted/logged. */
class OpenAiCompatibleChatClient {
    suspend fun reply(
        endpoint: String,
        apiKey: String,
        model: String,
        turns: List<Turn>
    ): String = withContext(Dispatchers.IO) {
        require(endpoint.startsWith("https://", ignoreCase = true)) {
            "استخدم عنوان HTTPS لحماية مفتاح API أثناء الإرسال."
        }
        require(apiKey.isNotBlank()) { "أدخل مفتاح API أولاً." }
        require(model.isNotBlank()) { "أدخل اسم النموذج." }

        val messages = JSONArray().put(
            JSONObject()
                .put("role", "system")
                .put("content", "أنت Glimmer، رفيق رقمي عربي ودود ومختصر. كن صريحاً بشأن حدودك ولا تدّع تنفيذ أفعال على الهاتف.")
        )
        turns.takeLast(MAX_TURNS).forEach { turn ->
            messages.put(
                JSONObject()
                    .put("role", if (turn.fromUser) "user" else "assistant")
                    .put("content", turn.text)
            )
        }
        val payload = JSONObject()
            .put("model", model)
            .put("messages", messages)
            .put("temperature", 0.7)

        val connection = URL(endpoint).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.doOutput = true
            connection.instanceFollowRedirects = false
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write(payload.toString()) }

            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            if (status !in 200..299) {
                val detail = runCatching { JSONObject(body).optJSONObject("error")?.optString("message") }.getOrNull()
                throw ChatRequestException(detail?.takeIf(String::isNotBlank) ?: "رفض مزوّد الذكاء الطلب (HTTP $status).")
            }
            val content = JSONObject(body)
                .optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
                ?.trim()
            content?.takeIf(String::isNotBlank)
                ?: throw ChatRequestException("وصل رد غير متوقع من مزوّد الذكاء.")
        } catch (exception: ChatRequestException) {
            throw exception
        } catch (exception: Exception) {
            throw ChatRequestException(
                when (exception) {
                    is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال. تحقق من الشبكة ثم أعد المحاولة."
                    else -> "تعذر الاتصال بالمزوّد. تحقق من عنوان HTTPS والشبكة وإعدادات المزوّد."
                },
                exception
            )
        } finally {
            connection.disconnect()
        }
    }

    data class Turn(val fromUser: Boolean, val text: String)

    class ChatRequestException(message: String, cause: Throwable? = null) : Exception(message, cause)

    private companion object {
        const val MAX_TURNS = 12
        const val CONNECT_TIMEOUT_MS = 15_000
        const val READ_TIMEOUT_MS = 45_000
    }
}
