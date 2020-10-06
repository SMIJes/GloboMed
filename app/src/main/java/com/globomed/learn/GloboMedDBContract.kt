package com.globomed.learn

import android.provider.BaseColumns
import android.provider.BaseColumns._ID

object GloboMedDbContract {

    object EmployeeEntry : BaseColumns {
        const val TABLE_NAME = "employee"
        const val COLUMN_ID = _ID
        const val COLUMN_NAME = "name"
        const val COLUMN_DOB = "dob"
        const val COLUMN_DESIGNATION = "designation"
        const val COLUMN_SURGEON = "is_surgeon"

        /*SQL statement to create the employee table*/
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                     COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT NOT NULL, " +
                    "$COLUMN_DOB INTEGER NOT NULL, " +
                    "$COLUMN_DESIGNATION TEXT NOT NULL)"

        //const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

        /*Alters version_1 of the database by adding a surgeon column
        to the employee table of the database*/
        const val ALTER_TABLE_1 = "ALTER TABLE " +
                "$TABLE_NAME " +
                "ADD COLUMN " +
                "$COLUMN_SURGEON INTEGER DEFAULT 0"
    }
}
