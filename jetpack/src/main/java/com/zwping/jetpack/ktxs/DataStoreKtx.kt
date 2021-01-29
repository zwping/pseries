package com.zwping.jetpack.ktxs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * DataStore扩展
 * zwping @ 1/11/21
 */

lateinit var dataStore: DataStore<Preferences>

inline fun <reified BasicType : Any> DataStore<Preferences>.get(key: String, default: BasicType): BasicType {
    var result = default

    runBlocking { data.first { result =  it[preferencesKey<BasicType>(key)] ?: default; true}  }
    return result
}

inline fun <reified BasicType : Any> DataStore<Preferences>.put(key: String, value: BasicType) {
    runBlocking { edit { it[preferencesKey<BasicType>(key)] = value } }
}