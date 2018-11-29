package com.raywenderlich.android.datadrop.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.raywenderlich.android.datadrop.app.DataDropApplication
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileRepository : DropRepository {

    private val gson: Gson
        get() {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Drop::class.java, DropTypeAdapter())
            return builder.create()
    }

    override fun addDrop(drop: Drop) {
        val string = gson.toJson(drop)
        try{
            val dropStream = dropOutStream(drop)
            dropStream.write(string.toByteArray())
            dropStream.close()
        }catch (e: IOException) {
            Log.e("FileRepository", "Error saving drop")
        }
    }

    override fun getDrops(): List<Drop> {
        return emptyList()
    }

    override fun clearDrop(drop: Drop) {

    }

    override fun clearAllDrops() {

    }

    private fun getContext() = DataDropApplication.getAppContext()

    private fun dropsDirectory() = getContext().getDir("drops", Context.MODE_PRIVATE)

    private fun dropFile(fileName: String) = File(dropsDirectory(), fileName)

    private fun dropFilename(drop: Drop) = drop.id + ".drop"

    private fun dropOutStream(drop: Drop) = FileOutputStream(dropFile(dropFilename(drop)))
}