package com.globomed.learn

import android.content.ContentValues
import com.globomed.learn.GloboMedDbContract.EmployeeEntry

object DataManager {

    //Fetches and returns and arrayList of all the employee entries in the dataBase
    fun fetchAllEmployees(databaseHelper: DatabaseHelper): ArrayList<Employee> {
        val db = databaseHelper.readableDatabase
        val employees = ArrayList<Employee>()

        //Queries the cursor and gets the position of each column
        val columns = arrayOf(
            EmployeeEntry.COLUMN_ID,
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION,
            EmployeeEntry.COLUMN_SURGEON
        )
        val cursor = db.query(EmployeeEntry.TABLE_NAME,columns,
            null,null,null,null,null)
        val idPos = cursor.getColumnIndex(EmployeeEntry.COLUMN_ID)
        val namePOs = cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPOs = cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos = cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)
        val surgeonPos = cursor.getColumnIndex(EmployeeEntry.COLUMN_SURGEON)

        //loops through the cursor, adding each employee entry to the employees arrayList
        while (cursor.moveToNext()) {
            val id = cursor.getString(idPos)
            val name = cursor.getString(namePOs)
            val dob = cursor.getLong(dobPOs)
            val designation = cursor.getString(designationPos)
            val surgeon = cursor.getInt(surgeonPos)
            employees.add(Employee(id, name, dob, designation, surgeon))
        }
        cursor.close()
        return employees
    }

    /*Gets the employee entry with the given employee ID (empId)*/
    fun fetchEmployee(databaseHelper: DatabaseHelper, empId: String): Employee? {
        val db = databaseHelper.readableDatabase
        var employee: Employee? = null

        /*Queries the cursor and gets the position of each column*/
        val columns = arrayOf(
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION,
            EmployeeEntry.COLUMN_SURGEON
        )
        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs: Array<String> = arrayOf(empId)
        //selects rows whose EmployeeEntry.COLUMN_ID == empId
        val cursor = db.query(EmployeeEntry.TABLE_NAME,columns,selection,selectionArgs,
            null,null,null)
        val namePOs = cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPOs = cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos = cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)
        val surgeonPos = cursor.getColumnIndex(EmployeeEntry.COLUMN_SURGEON)

        //moves cursor from index -1 to 0, gets th entry and assigns it to the employee variable
        while (cursor.moveToNext()) {
            val name = cursor.getString(namePOs)
            val dob = cursor.getLong(dobPOs)
            val designation = cursor.getString(designationPos)
            val surgeon = cursor.getInt(surgeonPos)
            employee = Employee(empId, name, dob, designation, surgeon)
        }
        cursor.close()
        return employee
    }

    /*Updates a given employee, using the employee ID as the matcher*/
    fun updateEmployee(databaseHelper: DatabaseHelper, employee: Employee) {
        val db = databaseHelper.writableDatabase

        val values = ContentValues()
        values.put(EmployeeEntry.COLUMN_NAME, employee.name)
        values.put(EmployeeEntry.COLUMN_DESIGNATION, employee.designation)
        values.put(EmployeeEntry.COLUMN_DOB, employee.dob)
        values.put(EmployeeEntry.COLUMN_SURGEON, employee.isSurgeon)

        /*This block of code says: select the row whose COLUMN_ID corresponds to employee.id and
        replace its values with contents of the variable values*/
        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs: Array<String> = arrayOf(employee.id)
        db.update(
            EmployeeEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

    }

    /*Deletes the employee entry with the given employee ID*/
    fun deleteEmployee(databaseHelper: DatabaseHelper, empId: String): Int {
        val db = databaseHelper.writableDatabase
        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs: Array<String> = arrayOf(empId)
        //returns the number of rows affected
        return db.delete(
            EmployeeEntry.TABLE_NAME,
            selection,
            selectionArgs
        )
    }

    /*Deletes all the entry in the table EmployeeEntry.TABLE_NAME*/
    fun deleteAllEmployee(databaseHelper: DatabaseHelper): Int {
        val db = databaseHelper.writableDatabase
        return db.delete(
            EmployeeEntry.TABLE_NAME,
            "1", //1 means the boolean true, hence returns true for all rows
            null
        )
    }
}