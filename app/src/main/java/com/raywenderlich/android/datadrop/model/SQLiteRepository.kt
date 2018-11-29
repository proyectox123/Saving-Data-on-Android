package com.raywenderlich.android.datadrop.model

import android.content.ContentValues
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.model.DropDbSchema.DropTable

class SQLiteRepository : DropRepository {

    private val database = DropDbHelper(DataDropApplication.getAppContext()).writableDatabase

    override fun addDrop(drop: Drop) {
        val contentValues = getDropContentValues(drop)
        database.insert(DropTable.NAME, null, contentValues)
    }

    override fun getDrops(): List<Drop> {
        return emptyList()
    }

    override fun clearDrop(drop: Drop) {

    }

    override fun clearAllDrops() {

    }

    private fun getDropContentValues(drop: Drop) =  ContentValues().apply {
        put(DropTable.Columns.ID, drop.id)
    }
}