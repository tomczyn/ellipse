package com.tomcz.mvi.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun View.clicks(): Flow<Unit> = callbackFlow {
    val listener = View.OnClickListener { trySend(Unit) }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
}

fun EditText.textChanged(): Flow<String> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(s?.toString() ?: "")
        }

        override fun afterTextChanged(s: Editable?) {
            // Nothing
        }
    }
    this@textChanged.addTextChangedListener(textWatcher)
    awaitClose {
        this@textChanged.removeTextChangedListener(textWatcher)
    }
}
