package com.android.project.ecotrans.response

import com.google.gson.annotations.SerializedName

data class ResponseAutoComplete(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
)

data class MatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
)

data class MainTextMatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
)

data class PredictionsItem(

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("matched_substrings")
	val matchedSubstrings: List<MatchedSubstringsItem?>? = null,

	@field:SerializedName("terms")
	val terms: List<TermsItem?>? = null,

	@field:SerializedName("structured_formatting")
	val structuredFormatting: StructuredFormatting? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)

data class TermsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class StructuredFormatting(

	@field:SerializedName("main_text_matched_substrings")
	val mainTextMatchedSubstrings: List<MainTextMatchedSubstringsItem?>? = null,

	@field:SerializedName("secondary_text")
	val secondaryText: String? = null,

	@field:SerializedName("main_text")
	val mainText: String? = null
)
