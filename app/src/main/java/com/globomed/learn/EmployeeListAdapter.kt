package com.globomed.learn

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.globomed.learn.GloboMedDbContract.EmployeeEntry
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class EmployeeListAdapter(private val context: Context?) :
	RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {
	lateinit var employeeList: ArrayList<Employee>
	//val TAG = EmployeeListAdapter::class.java.name

	/*Default RecycleView.Adapter methods; just implemented here*/
	override fun onCreateViewHolder(parent: ViewGroup,viewType: Int	): EmployeeViewHolder {
		val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent,
			false)
		return EmployeeViewHolder(itemView)
	}
	override fun getItemCount(): Int  = employeeList.size
	override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
		val employee = employeeList[position]
		holder.setData(employee.name, employee.designation, employee.isSurgeon, position)
		holder.setListener()
	}

	/*Updates the adapter's arrayList and notifies it*/
	fun setEmployees(employees: ArrayList<Employee>){
		employeeList = employees
		notifyDataSetChanged()
	}

	/*The ViewHolder class*/
	inner class EmployeeViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {

		private var pos = 0

		//Sets data to their respective views
		fun setData(name: String, designation: String, isSurgeon:Int, position: Int) {
			itemView.tvEmpName.text = name
			itemView.tvEmpDesignation.text = designation
			itemView.tvIsSurgeonConfirm.text =
				if(1==isSurgeon) "YES"
				else "NO"
			this.pos = position
		}

		//Sets the OnClickListener to the itemView: Starts the UpdateEmployee activity
		fun setListener()  {
			itemView.setOnClickListener {
				val intent = Intent(context, UpdateEmployeeActivity::class.java)
				intent.putExtra(EmployeeEntry.COLUMN_ID, employeeList[pos].id)
				(context as Activity).startActivityForResult(intent, 2)
			}
		}
	}
}
