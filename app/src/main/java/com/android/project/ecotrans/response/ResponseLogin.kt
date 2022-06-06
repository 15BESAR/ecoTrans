package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class LoginResult(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
