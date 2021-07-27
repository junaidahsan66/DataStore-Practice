package com.aexample.test_notifi.DataStore.Proto

import androidx.datastore.core.Serializer
import com.example.application.SaveUserData
import java.io.InputStream
import java.io.OutputStream

object UserSerializerProto : Serializer<SaveUserData>{
    override val defaultValue: SaveUserData
        get() = SaveUserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SaveUserData {
        return SaveUserData.parseFrom(input)
    }

    override suspend fun writeTo(t: SaveUserData, output: OutputStream) {
        return t.writeTo(output)
    }

//    override val defaultValue: SaveUserData
//        get() = SaveUserData.getDefaultInstance()
//
//    override suspend fun readFrom(input: InputStream): SaveUserData {
//        return SaveUserData.parseFrom(input)
//    }
//
//    override suspend fun writeTo(t: SaveUserData, output: OutputStream) {
//        return t.writeTo(output)
//    }
}