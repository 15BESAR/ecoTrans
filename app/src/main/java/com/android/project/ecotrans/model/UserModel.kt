package com.android.project.ecotrans.model

data class UserModel(
    val id: String,
    val username: String,
    val isLogin: Boolean,
    val token: String
)