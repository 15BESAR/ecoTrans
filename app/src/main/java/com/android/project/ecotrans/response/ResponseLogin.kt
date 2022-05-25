package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
