package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseVoucher(

	@field:SerializedName("voucherName")
	var voucherName: String? = null,

	@field:SerializedName("partnerName")
	var partnerName: String? = null,

	@field:SerializedName("price")
	var price: Int? = null,

	@field:SerializedName("imageUrl")
	var imageUrl: String? = null,

	@field:SerializedName("voucherDesc")
	var voucherDesc: String? = null,

	@field:SerializedName("partnerID")
	var partnerID: String? = null,

	@field:SerializedName("category")
	var category: String? = null,

	@field:SerializedName("stock")
	var stock: Int? = null
)
