package com.raywenderlich.android.datadrop.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class DropTypeAdapter : TypeAdapter<Drop>() {

    override fun read(reader: JsonReader?): Drop? {
        return null
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, drop: Drop) {
        with(out){
            beginObject()
            name("latitude").value(drop.latLng.latitude)
            name("longitude").value(drop.latLng.longitude)
            name("dropMessage").value(drop.dropMessage)
            name("id").value(drop.id)
            endObject()
        }
    }
}