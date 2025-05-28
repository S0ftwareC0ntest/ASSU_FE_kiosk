package com.example.assu_fe_kiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_kiosk.databinding.ItemFoodHeaderBinding
import com.example.assu_fe_kiosk.databinding.ItemMenusBinding

class FoodAdapter (
    val items: List<FoodListItem>,
    private val onItemClick: (FoodItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int = when(items[position]) {
        is FoodListItem.Header -> TYPE_HEADER
        is FoodListItem.Item -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER){
            val binding = ItemFoodHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        }
        else{
            val binding = ItemMenusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = items[position]){
            is FoodListItem.Header -> (holder as HeaderViewHolder).bind(item.title)
            is FoodListItem.Item -> (holder as ItemViewHolder).bind(item.food)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemFoodHeaderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(title: String){
            binding.tvCategoryHeader.text = title
        }
    }

    inner class ItemViewHolder(private val binding: ItemMenusBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: FoodItem){
            binding.tvGridItemMenuTitle.text = item.name
            binding.tvGridItemPrice.text = "%,d원".format(item.price)
            binding.ivFoodImage.setImageResource(item.imageResId)

            if(!item.available){
                binding.viewGridItemOverlayUnavailable.visibility = View.VISIBLE
                binding.tvGridItemOverlayUnavailableText.visibility = View.VISIBLE
                binding.root.isClickable = false
            }
            else{
                binding.viewGridItemOverlayUnavailable.visibility = View.GONE
                binding.tvGridItemOverlayUnavailableText.visibility = View.GONE
                binding.root.setOnClickListener { onItemClick(item) }
            }
        }
    }
}