package com.android.project.ecotrans.model

data class UserModel(
    val id: String,
    val name: String,
    val isLogin: Boolean,
    val token: String
)