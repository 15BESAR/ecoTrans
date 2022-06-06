package com.android.project.ecotrans.response

import com.android.project.ecotrans.model.User
import com.google.gson.annotations.SerializedName

data class ResponseUpdateUser(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("msg")
	val status: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)
