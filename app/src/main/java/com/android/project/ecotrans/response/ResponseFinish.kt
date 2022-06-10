package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseFinish(

	@field:SerializedName("reward")
	val reward: Int? = null,

	@field:SerializedName("emissionSaved")
	val emissionSaved: Double? = null,

	@field:SerializedName("finishTime")
	val finishTime: String? = null,

	@field:SerializedName("distanceTravelled")
	val distanceTravelled: Double? = null,

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("destination")
	val destination: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("journeyId")
	val journeyId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
