package com.aexample.test_notifi.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


class UserPref(val cn: Context) {

    companion object {
        val USER_AGE_KEY = intPreferencesKey("USER_AGE")
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "StoreDataDB")

    suspend fun addData(userName: String, age: Int) {
        cn.dataStore.edit {
            it[USER_NAME_KEY] = userName
            it[USER_AGE_KEY] = age


        }

    }

    val getName = cn.dataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }
    val getAge = cn.dataStore.data.map {
        it[USER_AGE_KEY] ?: 0
    }
}