package com.raywenderlich.android.datadrop.model

import android.content.ContentValues
import android.util.Log
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.model.DropDbSchema.DropTable
import java.io.IOException

class SQLiteRepository : DropRepository {

    private val database = DropDbHelper(DataDropApplication.getAppContext()).writableDatabase

    override fun addDrop(drop: Drop) {
        val contentValues = getDropContentValues(drop)
        database.insert(DropTable.NAME, null, contentValues)
    }

    override fun getDrops(): List<Drop> {
        val drops = mutableListOf<Drop>()
        val cursor = queryDrops(null, null)

        try{
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                drops.add(cursor.getDrop())
                cursor.moveToNext()
            }
        }catch (e: IOException){
            Log.e("SQLiteRepository", "Error reading drops")
        }finally {
            cursor.close()
        }

        return drops
    }

    override fun clearDrop(drop: Drop) {
        database.delete(
                DropTable.NAME,
                DropTable.Columns.ID + " = ?",
                arrayOf(drop.id)
        )
    }

    override fun clearAllDrops() {

    }

    private fun getDropContentValues(drop: Drop) =  ContentValues().apply {
        put(DropTable.Columns.ID, drop.id)
        put(DropTable.Columns.LATITUDE, drop.latLng.latitude)
        put(DropTable.Columns.LONGITUDE, drop.latLng.longitude)
        put(DropTable.Columns.DROP_MESSAGE, drop.dropMessage)
    }

    private fun queryDrops(where: String?, whereArgs: Array<String>?): DropCursorWrapper{
        val cursor = database.query(
                DropTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        )

        return DropCursorWrapper(cursor)
    }
}