package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseGetAllPurchaseHistory(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("purchases")
	val purchases: List<PurchasesItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class PurchasesItem(

	@field:SerializedName("voucherId")
	val voucherId: String? = null,

	@field:SerializedName("purchaseId")
	val purchaseId: String? = null,

	@field:SerializedName("buyDate")
	val buyDate: String? = null,

	@field:SerializedName("buyQuantity")
	val buyQuantity: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
