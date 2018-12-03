package com.raywenderlich.android.datadrop.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues
import android.util.Log
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.model.DropDbSchema.DropTable
import com.raywenderlich.android.datadrop.viewmodel.ClearAllDropsListener
import com.raywenderlich.android.datadrop.viewmodel.ClearDropListener
import com.raywenderlich.android.datadrop.viewmodel.DropInsertListener
import java.io.IOException

class SQLiteRepository : DropRepository {

    private val database = DropDbHelper(DataDropApplication.getAppContext()).writableDatabase

    override fun addDrop(drop: Drop, listener: DropInsertListener) {
        val contentValues = getDropContentValues(drop)
        val result = database.insert(DropTable.NAME, null, contentValues)
        if(result != -1L){
            listener.dropInserted(drop)
        }
    }

    override fun getDrops(): LiveData<List<Drop>> {
        val liveData = MutableLiveData<List<Drop>>()
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

        liveData.value = drops

        return liveData
    }

    override fun clearDrop(drop: Drop, listener: ClearDropListener) {
        val result = database.delete(
                DropTable.NAME,
                DropTable.Columns.ID + " = ?",
                arrayOf(drop.id)
        )
        if(result != 0){
            listener.dropsCleared(drop)
        }
    }

    override fun clearAllDrops(listener: ClearAllDropsListener) {
        val result = database.delete(
                DropTable.NAME,
                null,
                null
        )
        if(result != 0){
            listener.allDropsCleared()
        }
    }

    private fun getDropContentValues(drop: Drop) =  ContentValues().apply {
        put(DropTable.Columns.ID, drop.id)
        put(DropTable.Columns.LATITUDE, drop.latLng.latitude)
        put(DropTable.Columns.LONGITUDE, drop.latLng.longitude)
        put(DropTable.Columns.DROP_MESSAGE, drop.dropMessage)
        put(DropTable.Columns.MARKER_COLOR, drop.markerColor)
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