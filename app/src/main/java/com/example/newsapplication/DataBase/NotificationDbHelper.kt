package com.example.newsapplication.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.newsapplication.DataClass.notificationDataClass
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider.DATABASE_NAME

class NotificationDbHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NotificationDB.db"
        private const val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${NotificationContract.NotificationEntry.TABLE_NAME} (
                ${NotificationContract.NotificationEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
                ${NotificationContract.NotificationEntry.COLUMN_NAME_TITLE} TEXT,
                ${NotificationContract.NotificationEntry.COLUMN_NAME_SUBTITLE} TEXT,
                ${NotificationContract.NotificationEntry.COLUMN_NAME_DATE} TEXT)
            """
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${NotificationContract.NotificationEntry.TABLE_NAME}"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertNotification(title: String, subtitle: String, date: String) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE, title)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_SUBTITLE, subtitle)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_DATE, date)
        }

        db.insert(NotificationContract.NotificationEntry.TABLE_NAME, null, values)
    }

    fun getAllNotifications(): ArrayList<notificationDataClass> {
        val notificationsList = ArrayList<notificationDataClass>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${NotificationContract.NotificationEntry.TABLE_NAME}", null)

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE)
                val subtitleIndex = cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_SUBTITLE)
                val dateIndex = cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_DATE)

                do {
                    // Check if column indices are valid
                    if (titleIndex != -1 && subtitleIndex != -1 && dateIndex != -1) {
                        val title = cursor.getString(titleIndex)
                        val subtitle = cursor.getString(subtitleIndex)
                        val date = cursor.getString(dateIndex)

                        val notification = notificationDataClass(title, subtitle, date)
                        notificationsList.add(notification)
                    }
                } while (cursor.moveToNext())
            }
        }

        return notificationsList
    }
}