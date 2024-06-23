package com.yawki.common.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {
    private val _events = MutableSharedFlow<Any>()
    val events: SharedFlow<Any> = _events.asSharedFlow()
    suspend fun emit(event: Any) {
        _events.emit(event)
    }
}