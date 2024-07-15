package com.example.gestor_de_tareas.models

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

suspend inline fun <T> DataStore<Preferences>.getValueSync(
    key: Preferences.Key<T>,
    defaultValue: T
): T = data.first()[key] ?: defaultValue

inline fun <T> DataStore<Preferences>.getValueFlow(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> = data.map { preferences ->
    preferences[key] ?: defaultValue
}

suspend inline fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}