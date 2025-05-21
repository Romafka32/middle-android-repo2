package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {
    companion object {
        private const val DELAY_FACTOR = 2
        private const val INITIAL_DELAY = 5000L
    }

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen {cause,attempt->
            if (cause is Exception) {
                val delayTime = INITIAL_DELAY * DELAY_FACTOR.toDouble().pow(attempt.toInt()).toLong()
                delay(delayTime)
                attempt < 2
            } else {
                false
            }
        }
    }
}