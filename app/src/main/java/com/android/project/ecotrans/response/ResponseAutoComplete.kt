package com.android.project.ecotrans.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseAutoComplete(

	@field:SerializedName("predictions")
	var predictions: List<PredictionsItem?>? = null
): Parcelable

@Parcelize
data class MatchedSubstringsItem(

	@field:SerializedName("offset")
	var offset: Int? = null,

	@field:SerializedName("length")
	var length: Int? = null
): Parcelable

@Parcelize
data class MainTextMatchedSubstringsItem(

	@field:SerializedName("offset")
	var offset: Int? = null,

	@field:SerializedName("length")
	var length: Int? = null
): Parcelable

@Parcelize
data class PredictionsItem(

	@field:SerializedName("types")
	var types: List<String?>? = null,

	@field:SerializedName("matched_substrings")
	var matchedSubstrings: List<MatchedSubstringsItem?>? = null,

	@field:SerializedName("terms")
	var terms: List<TermsItem?>? = null,

	@field:SerializedName("structured_formatting")
	var structuredFormatting: StructuredFormatting? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("place_id")
	var placeId: String? = null
): Parcelable

@Parcelize
data class TermsItem(

	@field:SerializedName("offset")
	var offset: Int? = null,

	@field:SerializedName("varue")
	var varue: String? = null
): Parcelable

@Parcelize
data class StructuredFormatting(

	@field:SerializedName("main_text_matched_substrings")
	var mainTextMatchedSubstrings: List<MainTextMatchedSubstringsItem?>? = null,

	@field:SerializedName("secondary_text")
	var secondaryText: String? = null,

	@field:SerializedName("main_text")
	var mainText: String? = null
): Parcelable
