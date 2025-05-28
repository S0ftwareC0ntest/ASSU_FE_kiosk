package com.example.assu_fe_kiosk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assu_fe_kiosk.databinding.ActivityHomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    val menuTypes = listOf("면류", "마른안주", "주류", "음료")
    lateinit var menuAdapter: MenuAdapter
    private lateinit var binding: ActivityHomeBinding

    private lateinit var foodList: List<FoodItem>

    private val bucketItemList = mutableListOf<BucketItem>()
    private lateinit var bucketAdapter: BucketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodList = listOf(
            FoodItem("짜파구리", 12000, R.drawable.img_menus_food_1, "면류"),
            FoodItem("짜파구리 범벅 세트", 12000, R.drawable.img_menus_food_1, "면류", false),
            FoodItem("짜파구리+김치전 대실속 미친 세트", 12000, R.drawable.img_menus_food_1, "면류"),
            FoodItem("짜파구리", 12000, R.drawable.img_menus_food_1, "면류"),
            FoodItem("짜파구리", 12000, R.drawable.img_menus_food_1, "면류"),
            FoodItem("짜파구리", 12000, R.drawable.img_menus_food_1, "면류"),
            FoodItem("먹태", 11000, R.drawable.img_menus_food_1, "마른안주"),
            FoodItem("먹태", 11000, R.drawable.img_menus_food_1, "마른안주"),
            FoodItem("먹태", 11000, R.drawable.img_menus_food_1, "마른안주"),
            FoodItem("먹태", 11000, R.drawable.img_menus_food_1, "마른안주"),
            FoodItem("먹태", 11000, R.drawable.img_menus_food_1, "마른안주"),
            FoodItem("소주", 5000, R.drawable.img_menus_food_1, "주류"),
            FoodItem("소주", 5000, R.drawable.img_menus_food_1, "주류"),
            FoodItem("소주", 5000, R.drawable.img_menus_food_1, "주류"),
            FoodItem("소주", 5000, R.drawable.img_menus_food_1, "주류"),
            FoodItem("소주", 5000, R.drawable.img_menus_food_1, "주류"),
            FoodItem("콜라", 5000, R.drawable.img_menus_food_1, "음료"),
            FoodItem("사이다", 5000, R.drawable.img_menus_food_1, "음료"),
        )

        val groupedList = groupFoodByCategory(foodList)
        val adapter = FoodAdapter(groupedList) { item ->
            val existingItem = bucketItemList.find{it.name == item.name}

            if(existingItem != null){
                existingItem.count++
            }
            else{
                bucketItemList.add(BucketItem(item.name, 1, item.price))
            }

            bucketAdapter.notifyDataSetChanged()
            updateTotalPrice()

            binding.rvSideBarSelectedMenuList.post {
                binding.rvSideBarSelectedMenuList.scrollToPosition(bucketItemList.size - 1)
            }
        }

        menuAdapter = MenuAdapter(menuTypes){index ->
            scrollToMenuCategory(menuTypes[index])
        }
        binding.rvNavigatorMenuSelect.adapter = menuAdapter
        binding.rvNavigatorMenuSelect.layoutManager = LinearLayoutManager(this)
        menuAdapter.setSelectedIndex(0)
        scrollToMenuCategory(menuTypes[0])

        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)){
                    0 -> 3
                    else -> 1
                }
            }
        }
        binding.rvMenusMenuList.layoutManager = layoutManager
        binding.rvMenusMenuList.adapter = adapter

        bucketAdapter = BucketAdapter(bucketItemList){
            updateTotalPrice()
        }
        binding.rvSideBarSelectedMenuList.adapter = bucketAdapter
        binding.rvSideBarSelectedMenuList.layoutManager = LinearLayoutManager(this)

        binding.rvMenusMenuList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val adapter = recyclerView.adapter as? FoodAdapter ?: return
                val item = adapter.items.getOrNull(firstVisible) ?: return

                if (item is FoodListItem.Header) {
                    val categoryIndex = menuTypes.indexOf(item.title)
                    if (categoryIndex != -1) {
                        menuAdapter.setSelectedIndex(categoryIndex)
                    }
                }
            }
        })
    }

    private fun updateTotalPrice(){
        val total = bucketItemList.sumOf { it.count * it.price }
        binding.tvSideBarTotalPrice.text = "%,d".format(total)
    }

    fun scrollToMenuCategory(category: String){
        val groupedList = (binding.rvMenusMenuList.adapter as? FoodAdapter)?.items ?: return

        val headerIndex = groupedList.indexOfFirst{
            it is FoodListItem.Header && it.title == category
        }

        if(headerIndex != -1){
            val layoutManager = binding.rvMenusMenuList.layoutManager as GridLayoutManager
            layoutManager.scrollToPositionWithOffset(headerIndex, 0)
        }
    }

    fun groupFoodByCategory(foodList: List<FoodItem>): List<FoodListItem>{
        val grouped = mutableListOf<FoodListItem>()
        foodList.groupBy { it.category }.forEach{ (category, items) ->
            grouped.add(FoodListItem.Header(category))
            grouped.addAll(items.map {FoodListItem.Item(it) })
        }
        return grouped
    }
}