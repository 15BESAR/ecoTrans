package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class PostResponseRegister(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
