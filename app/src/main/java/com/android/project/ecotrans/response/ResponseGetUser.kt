package com.android.project.ecotrans.response

import com.android.project.ecotrans.model.User
import com.google.gson.annotations.SerializedName

data class ResponseGetUser(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("user")
	val user: User? = null
)
