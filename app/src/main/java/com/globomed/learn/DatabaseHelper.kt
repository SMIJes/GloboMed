package com.globomed.learn

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.globomed.learn.GloboMedDbContract.EmployeeEntry

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {

    //Called at the creation of the app
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(EmployeeEntry.SQL_CREATE_ENTRIES)
    }

    //Called at whenever the version number (DATABASE_VERSION) is changed
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /*This block of code will just alter the schema and contents of the existing table*/
        db?.execSQL(EmployeeEntry.ALTER_TABLE_1)

        /*This block of code will drop the existing table and create a new one*/
        //db?.execSQL(GloboMedDbContract.EmployeeEntry.SQL_DROP_TABLE)
        //onCreate(db)
    }

    //Holds the DATABASE_NAME and DATABASE_VERSION
    companion object {
        const val DATABASE_NAME = "globomed.db"

        /*This is th older version*/
        //const val DATABASE_VERSION = 1

        /*This is the new/current version. On detecting change in version number, the OpenHelper
        * will determine whether the onUpgrade or onDowngrade method will be called */
        const val DATABASE_VERSION = 2
    }
}