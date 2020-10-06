package com.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private val employeeListAdapter = EmployeeListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)

        /*Sets up the recyclerView and its adapter*/
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = employeeListAdapter
        employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))

        /*Sets OnClickListener for th floating action button*/
        fab.setOnClickListener {
            val addEmployee = Intent(this, AddEmployeeActivity::class.java)
            startActivityForResult(addEmployee, 1)
        }
    }

    /*Called whenever an IntentForResult returns to this activity*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*Means that whenever an IntentForResult returns to this activity, regardless which intent
        it was, the set employee method will be called on the employee adapter. This effectively
        updates the RecyclerView from the database*/
        if(resultCode == Activity.RESULT_OK){
            employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))
        }
    }

    /*Handles the optionMenu creation in this activity*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /*Handles the Menu item selection from the optionMenu in this activity*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*Creates an AlertDialog  whose +ve button deletes all entries from the database, makes
        toast, and set the recyclerAdapter to empty arrayList while the -ve button simply dismisses
        the dialog*/
        return when (item.itemId){
            R.id.action_deleteAll -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.confirm_sure)
                    .setPositiveButton(R.string.yes){ _, _ ->
                        val result = DataManager.deleteAllEmployee(databaseHelper)
                        Toast.makeText(applicationContext, "$result record(s) deleted",
                            Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK, Intent())
                        employeeListAdapter.setEmployees(ArrayList())
                        employeeListAdapter.notifyDataSetChanged()
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
