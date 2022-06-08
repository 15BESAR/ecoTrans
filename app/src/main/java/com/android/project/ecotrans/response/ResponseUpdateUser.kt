package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseUpdateUser(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("voucherInterest")
	val voucherInterest: String? = null,

	@field:SerializedName("income")
	val income: Int? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("marriageStatus")
	val marriageStatus: Boolean? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("points")
	val points: Int? = null,

	@field:SerializedName("vehicle")
	val vehicle: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("Journeys")
	val journeys: Any? = null,

	@field:SerializedName("domicile")
	val domicile: String? = null,

	@field:SerializedName("job")
	val job: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)
