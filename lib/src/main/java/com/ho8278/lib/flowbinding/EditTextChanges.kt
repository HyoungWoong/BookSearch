package com.ho8278.lib.flowbinding

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun EditText.textChanges(): Flow<String> {
    return callbackFlow<String> {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Not in main thread." }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trySend(p0?.toString().orEmpty())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }

        addTextChangedListener(textWatcher)

        awaitClose { removeTextChangedListener(textWatcher) }
    }
        .onStart { text }
}