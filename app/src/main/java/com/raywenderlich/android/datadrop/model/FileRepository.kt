package com.raywenderlich.android.datadrop.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.raywenderlich.android.datadrop.app.DataDropApplication
import java.io.*

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
        val drops = mutableListOf<Drop>()

        try{
            val fileList = dropsDirectory().list()

            fileList.map { convertStreamToString(dropInputStream(it)) }
                    .mapTo(drops){ gson.fromJson(it, Drop::class.java)}
        }catch (e: IOException){
            Log.e("FileRepository", "Error reading drops")
        }

        return drops
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

    private fun dropInputStream(fileName: String) = FileInputStream(dropFile(fileName))

    @Throws(IOException::class)
    private fun convertStreamToString(inputStream: InputStream): String{
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while(line != null){
            sb.append(line).append("\n")
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }
}