package com.example.assu_fe_kiosk

data class FoodItem(
    val name: String,
    val price: Int,
    val imageResId: Int,
    val category: String,
    val available: Boolean = true
)
