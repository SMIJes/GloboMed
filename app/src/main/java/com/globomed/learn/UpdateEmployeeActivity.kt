package com.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*
import com.globomed.learn.GloboMedDbContract.EmployeeEntry


class UpdateEmployeeActivity: AppCompatActivity() {

	private lateinit var databaseHelper : DatabaseHelper
	private val myCalendar = Calendar.getInstance()
	private var empId: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

		databaseHelper = DatabaseHelper(this)

		/*Gets the employee ID from the extras (in the bundle) from the intent that started this
		activity, and fetches the corresponding employee from the database then sets the elements
		of this employee to their respective fields*/
		val bundle = intent.extras
		bundle?.let{
			empId = bundle.getString(EmployeeEntry.COLUMN_ID)
			//!! is the non-null exception operator
			val employee = DataManager.fetchEmployee(databaseHelper, empId!!)
			employee?.let {
				etEmpName.setText(employee.name)
				etDesignation.setText(employee.designation)
				etDOB.setText(getFormattedDate(employee.dob))
				sSurgeon.isChecked = (1==employee.isSurgeon)
			}
		}

		/*This date is an OnClickListener variable; it takes the values from the DatePickerDialogue
		and sets them to the calender variable, then sets the formatted date to the etDOB*/
		val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
			myCalendar.set(Calendar.YEAR, year)
			myCalendar.set(Calendar.MONTH, monthOfYear)
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
			etDOB.setText(getFormattedDate(myCalendar.timeInMillis))
		}

		//sets the onClickListener for the views and buttons
		bSave.setOnClickListener {
			saveEmployee()
		}
		etDOB.setOnClickListener {
			setUpCalender(date)
		}
		bCancel.setOnClickListener {
			finish()
		}
	}

	/*Saves an employee detail from the input fields to the database
	This is similar to the same method on AddEmployeeActivity*/
	private fun saveEmployee() {

		var isValid = true

		etEmpName.error = if (etEmpName?.text.toString().isEmpty()) {
			isValid = false
			"Required Field"
		} else null
		etDesignation.error = if (etDesignation?.text.toString().isEmpty()) {
			isValid = false
			"Required Field"
		} else null

		if (isValid) {
			val updateName = etEmpName.text.toString()
			val updateDOB = myCalendar.timeInMillis
			val updateDesignation = etDesignation.text.toString()
			val updateIsSurgeon = if(sSurgeon.isChecked) 1 else 0
			val updatedEmployee = Employee(empId!!, updateName, updateDOB, updateDesignation, updateIsSurgeon)
			DataManager.updateEmployee(databaseHelper,updatedEmployee)

			setResult(Activity.RESULT_OK, Intent())
			Toast.makeText(applicationContext, "Employee Updated", Toast.LENGTH_SHORT).show()
			finish()
		}
	}

	/*Executes the date variable (a listener) and sets up and displays the DatePickerDialogue
    based on the values of the calender variable*/
	private fun setUpCalender(date: DatePickerDialog.OnDateSetListener) {
		DatePickerDialog(
			this, date, myCalendar
			.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
			myCalendar.get(Calendar.DAY_OF_MONTH)
		).show()
	}

	//Formats the dateOfBirth from Milliseconds to humanReadable format eg: 10 Oct, 2020
	private fun getFormattedDate(dobInMilis: Long?): String {
		return dobInMilis?.let {
			val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
			sdf.format(dobInMilis)
		} ?: "Not Found"
	}

	/*Handles creation of the OptionsMenu in this activity*/
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_item, menu)
		return true
	}

	/*Similar to the same method in MainActivity but doesn't explicitly calls for an update of the
	 RecyclerAdapter because on the call of finish(), MainActivity(the activity which started this
	 activity) is called. The onCreate method of MainActivity contains code to setUp the
	 RecyclerAdapter according to the current state of the database*/
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId){
			R.id.action_delete -> {
				val builder = AlertDialog.Builder(this)
				builder.setMessage(R.string.confirm_sure)
					.setPositiveButton(R.string.yes){
							_, _ ->

						val result = DataManager.deleteEmployee(databaseHelper, empId.toString())
						Toast.makeText(applicationContext, "$result record(s) deleted",
							Toast.LENGTH_SHORT).show()
						setResult(Activity.RESULT_OK, Intent())
						finish()
					}
					.setNegativeButton(R.string.no){ dialog, _ -> dialog.dismiss()}

				val dialog = builder.create()
				dialog.setTitle("Are you sure")
				dialog.show()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}