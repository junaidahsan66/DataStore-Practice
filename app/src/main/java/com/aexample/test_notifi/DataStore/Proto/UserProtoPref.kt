package com.aexample.test_notifi.DataStore.Proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.application.SaveUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.lang.Exception

class UserProtoPref(val context: Context) {
    private val Context.dataStore: DataStore<SaveUserData> by dataStore(
        fileName = "localDB",
        serializer = UserSerializerProto
    )

    //write to proto data store
    suspend fun writeToLocal(name : String , age :Int) {
        context.dataStore.updateData {
            it.toBuilder()
                .setName(name)
                .setAge(age).build()
        }
    }

    val readFromLocal : Flow<dataSave> = context.dataStore.data.catch {
        if (this is Exception){
            //Do what eva u wnt
        }
    }.map {
        return@map dataSave(it.name, it.age)
    }
}

data class dataSave(val name:String,val age : Int)