package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponsePurchaseVoucher(

	@field:SerializedName("receipt")
	val receipt: Receipt? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class Receipt(

	@field:SerializedName("userPointsRemaining")
	val userPointsRemaining: Int? = null,

	@field:SerializedName("voucherId")
	val voucherId: String? = null,

	@field:SerializedName("purchaseId")
	val purchaseId: String? = null,

	@field:SerializedName("buyDate")
	val buyDate: String? = null,

	@field:SerializedName("voucherStockRemaining")
	val voucherStockRemaining: Int? = null,

	@field:SerializedName("buyQuantity")
	val buyQuantity: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
