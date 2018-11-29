package com.raywenderlich.android.datadrop.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class DropTypeAdapter : TypeAdapter<Drop>() {

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Drop {
        var latitude = 0.0
        var longitude = 0.0
        var dropMessage = ""
        var id = ""

        with(reader){
            beginObject()
            while(hasNext()){
                when(reader.nextName()){
                    "latitude" -> latitude = nextDouble()
                    "longitude" -> longitude = nextDouble()
                    "dropMessage" -> dropMessage = nextString()
                    "id" -> id = nextString()
                }
            }
            endObject()
        }

        return Drop(LatLng(latitude, longitude), dropMessage, id)
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