package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseVersion(

	@field:SerializedName("lastUpdate")
	val lastUpdate: String? = null,

	@field:SerializedName("version")
	val version: String? = null
)
