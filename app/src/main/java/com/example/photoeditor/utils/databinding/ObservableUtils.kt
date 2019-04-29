package com.example.photoeditor.utils.databinding

import androidx.databinding.ObservableMap

fun <K, V> ObservableMap<K, V>.withChangedCallback(callback: (source: Map<K, V>, key: K) -> Unit): ObservableMap<K, V> {
    addOnMapChangedCallback(object : ObservableMap.OnMapChangedCallback<ObservableMap<K, V>, K, V>() {
        override fun onMapChanged(sender: ObservableMap<K, V>, key: K) {
            callback(sender, key)
        }
    })

    return this
}