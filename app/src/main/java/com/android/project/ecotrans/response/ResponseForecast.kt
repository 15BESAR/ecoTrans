package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseForecast(

	@field:SerializedName("forecast")
	val forecast: Forecast? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class Forecast(

	@field:SerializedName("uv")
	val uv: Int? = null,

	@field:SerializedName("temp")
	val temp: Double? = null,

	@field:SerializedName("aqi")
	val aqi: Double? = null,

	@field:SerializedName("time")
	val time: Int? = null
)
