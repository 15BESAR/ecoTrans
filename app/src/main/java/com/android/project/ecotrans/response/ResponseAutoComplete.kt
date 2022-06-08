package com.android.project.ecotrans.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAutoComplete(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
) : Parcelable

@Parcelize
data class MainTextMatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
) : Parcelable

@Parcelize
data class TermsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Parcelable

@Parcelize
data class PredictionsItem(

	@field:SerializedName("types")
	var types: List<String?>? = null,

	@field:SerializedName("matched_substrings")
	val matchedSubstrings: List<MatchedSubstringsItem?>? = null,

	@field:SerializedName("terms")
	val terms: List<TermsItem?>? = null,

	@field:SerializedName("structured_formatting")
	val structuredFormatting: StructuredFormatting? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("place_id")
	var placeId: String? = null
) : Parcelable

@Parcelize
data class StructuredFormatting(

	@field:SerializedName("main_text_matched_substrings")
	val mainTextMatchedSubstrings: List<MainTextMatchedSubstringsItem?>? = null,

	@field:SerializedName("secondary_text")
	val secondaryText: String? = null,

	@field:SerializedName("main_text")
	val mainText: String? = null
) : Parcelable

@Parcelize
data class MatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
) : Parcelable
