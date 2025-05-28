package com.example.assu_fe_kiosk

sealed class FoodListItem{
    data class Header(val title: String): FoodListItem()
    data class Item(val food: FoodItem): FoodListItem()
}
