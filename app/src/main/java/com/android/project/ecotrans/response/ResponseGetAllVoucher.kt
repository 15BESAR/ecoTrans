package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseGetAllVoucher(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("vouchers")
	val vouchers: List<VouchersItem?>? = null
)

data class VouchersItem(

	@field:SerializedName("voucherName")
	val voucherName: String? = null,

	@field:SerializedName("partnerName")
	val partnerName: String? = null,

	@field:SerializedName("voucherId")
	val voucherId: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("voucherDesc")
	val voucherDesc: String? = null,

	@field:SerializedName("partnerId")
	val partnerId: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null
)
