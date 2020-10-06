package com.globomed.learn

//This represents the Employee object used throughout this app
class Employee (
    val id: String,
    val name: String,
    val dob: Long,
    val designation: String,
    val isSurgeon:Int){

    override fun toString(): String {
        return " \n id: $id,\n name: $name,\n DOB: $dob,\n designation: $designation\n"
    }
}