package com.globomed.learn

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*
import com.globomed.learn.GloboMedDbContract.EmployeeEntry

class AddEmployeeActivity : Activity() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        databaseHelper = DatabaseHelper(this)

        /*This is a listener. Sets values to the calender variable and text to the etDOB editTex
        On clicking ok on the calender dialog, this block of code sets the input on
        the dialogue to the etDOB, as formatted by the getFormattedDate function*/
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etDOB.setText(getFormattedDate(myCalendar.timeInMillis))
        }

        //These blocks of codes sets OnClickListeners to the editTexts and Buttons as follows
        etDOB.setOnClickListener {
            setUpCalender(date)
        }
        bSave.setOnClickListener {
            saveEmployee()
        }
        bCancel.setOnClickListener {
            finish()
        }
    }

    /*Executes the date variable (a listener) and sets up and displays the DatePickerDialogue
    based on the values of the calender variable*/
    private fun setUpCalender(date: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(this,
            date,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /*Saves an employee detail from the input fields to the database*/
    private fun saveEmployee() {

        /*Ensures that the required fields (etEmpName and etDesignation) are both filled
        (the etDOB defaults to current date) and indicates this via the isValid variable*/
        var isValid = true
        etEmpName.error = if (etEmpName?.text.toString().isEmpty()) {
            isValid = false
            "Required Field"
        } else null
        etDesignation.error = if (etDesignation?.text.toString().isEmpty()) {
            isValid = false
            "Required Field"
        } else null

        /*Confirms that the required fields are filled, getTexts from all the fields, creates a
        contentValues variables and inserts it to the database and then ends the activity*/
        if (isValid) {
            val name = etEmpName?.text.toString()
            val designation = etDesignation?.text.toString()
            val dob = myCalendar.timeInMillis
            val isSurgeon = if(sSurgeon.isChecked) 1 else 0

            val values = ContentValues()
            values.put(EmployeeEntry.COLUMN_NAME, name)
            values.put(EmployeeEntry.COLUMN_DESIGNATION, designation)
            values.put(EmployeeEntry.COLUMN_DOB, dob)
            values.put(EmployeeEntry.COLUMN_SURGEON, isSurgeon)

            val db = databaseHelper.writableDatabase
            val result = db.insert(EmployeeEntry.TABLE_NAME,null, values)

            setResult(RESULT_OK, Intent())
            Toast.makeText(applicationContext, "Employee Added at row $result",
                Toast.LENGTH_LONG).show()
        }
        finish()
    }

    //Formats the dateOfBirth from Milliseconds to humanReadable format eg: 10 Oct, 2020
    private fun getFormattedDate(dobInMillis: Long?): String {
        return dobInMillis?.let {
            val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
            sdf.format(dobInMillis)
        } ?: "Not Found"
    }
}
